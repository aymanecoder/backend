package com.example.demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import weka.attributeSelection.*;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.*;
import weka.clusterers.*;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.stemmers.LovinsStemmer;
import weka.core.stopwords.MultiStopwords;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;
public class model {




        private Instances  data;
        private Map<String, String> forms;

        public model(Instances data) {
            this.data = data;
        }

        public model(Instances data, Map<String, String> forms) {
            this.data = data;
            this.forms = forms;
        }

        public Instances getData() {
            return this.data;
        }
        public void setData(Instances data) {
            this.data = data;
        }
        public Map<String, String> getForms() {
            return this.forms;
        }
        public void setForms(Map<String, String> forms) {
            this.forms = forms;
        }

        public double String2Double(Instances data, String key, String value) {

            int indexofattribute = data.attribute(key).index();
            double[] cols = data.attributeToDoubleArray(indexofattribute);

            int indexofvalue = data.attribute(key).indexOfValue(value);
            return cols[indexofvalue];
        }


        public LinkedList<String> giveInstances(Instances dataOriginal) throws Exception {
            data = new Instances(this.data);

            //Remove unused attributes
            List<Integer> indicesofAttributesToBeRemoved = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,14,15,21,22,23,24,25,26,27,28,29);
            String[] opts = new String[] { "-R", String.join(",", indicesofAttributesToBeRemoved.stream().map(Object::toString).collect(Collectors.toList())) };
            Remove remove = new Remove();
            remove.setOptions(opts);
            remove.setInputFormat(data);
            data = Filter.useFilter(data, remove);


            /*
             * Build Cluster
             */
            // new instance of clusterer
            SimpleKMeans model = new SimpleKMeans();
            model.setNumClusters(3);
            EuclideanDistance dist = new EuclideanDistance();
            model.setDistanceFunction(dist);
            // build the clusterer
            model.buildClusterer(data);
            //System.out.println(model);

            //double logLikelihood = ClusterEvaluation.crossValidateModel((DensityBasedClusterer) model, data, 10, new Random(1));
            //System.out.println("Likelihood = "+logLikelihood);

            /*
             * Add a new attribute to the data to store the cluster assignments
             */
            //Obtain the cluster assignments for each instance
            ClusterEvaluation eval = new ClusterEvaluation();
            eval.setClusterer(model);
            eval.evaluateClusterer(data);

            double[] clusters = eval.getClusterAssignments();

            AddCluster addFilter = new AddCluster();
            addFilter.setClusterer(model);
            addFilter.setInputFormat(data);
            data = Filter.useFilter(data, addFilter);

            // Set the cluster assignments for each instance
            for (int i = 0; i < data.numInstances(); i++) {
                data.instance(i).setValue(data.numAttributes() - 1, clusters[i]);
            }


            /*
             * Build Classification model
             */
            //Set classifier attribute index
            data.setClassIndex(data.numAttributes() - 1);

            //Features selection
            AttributeSelection attSelect = new AttributeSelection();
            CfsSubsetEval eval1 = new CfsSubsetEval();
            GreedyStepwise search = new GreedyStepwise();
            attSelect.setEvaluator(eval1);
            attSelect.setSearch(search);
            attSelect.SelectAttributes(data);
            int[] indices = attSelect.selectedAttributes();
            //System.out.println("Selected attributes: "+Utils.arrayToString(indices));

            /*
             * Build a decision tree
             */
            String[] options = new String[1];
            options[0] = "-U";
            J48 tree = new J48();
            tree.setOptions(options);
            tree.buildClassifier(data);
            //System.out.println(tree);

            /*
             * Classify an instance.
             */
            double[] vals = new double[data.numAttributes()];
            int i=0;
            for (Map.Entry m : this.forms.entrySet()) {
                vals[i]=String2Double(data, (String) m.getKey(), (String) m.getValue());
                i++;
            }
		/*
		vals[0] = 0.0; // Region {"Maroc"}
		vals[1] = 1.0; // ville {" Casablanca "," Rabat "}
		vals[2] = 0.0; // Secteur_activite {"Informatique"}
		vals[3] = 0.0; // Type_du_contrat {"CDI"}
		vals[4] = 0.0; // Niveau_etudes {"Bac +5 et plus"}
		vals[5] = 0.0; // Experience {"5 ans","4 ans"}
		vals[6] = 0.0; // Teletravail {" Hybride"," Non"}
		*/
            Instance myUnicorn = new DenseInstance(1.0, vals);
            //Assosiate your instance with Instance object in this case dataRaw
            myUnicorn.setDataset(data);

            double label = tree.classifyInstance(myUnicorn);
            String Predicted_Cluster = data.classAttribute().value((int) label);
            System.out.println("Your classe is: " + Predicted_Cluster);

            /*
             * Recommanded data
             */
            // specify the filter
            opts = new String[]{"-R", String.valueOf(data.classIndex())};
            remove = new Remove();
            remove.setOptions(opts);
            remove.setInputFormat(data);

            // apply the filter
            Instances ClassCol = Filter.useFilter(data, remove);

            String classValue;
            LinkedList<String> RecommandedData = new LinkedList<>();

            List<String> attributes = new ArrayList<>();
            for(i=0; i<dataOriginal.numAttributes();i++) {
                attributes.add(dataOriginal.attribute(i).name());
            }
            RecommandedData.add(String.join(",", attributes));

            // loop through all the instances in the filtered data
            for (i = 0; i < ClassCol.numInstances(); i++) {
                // get the class attribute
                classValue = ClassCol.instance(i).stringValue(ClassCol.numAttributes() - 1);

                // check if the class attribute is equal to "cluster1"
                if (classValue.equals(Predicted_Cluster)) {
                    // do something
                    //System.out.println("line = " + i);
                    RecommandedData.add(dataOriginal.instance(i).toString());
                }
            }
            return RecommandedData;

        }

        public static void main(String[] args) throws Exception {
            DataSource source = new DataSource("com/example/demo/model/data1.arff");
            Instances data = source.getDataSet();
            System.out.println(data.numInstances() + " instances loaded.");

            Map<String, String> forms = new HashMap<String, String>();
            forms.put("Region", "Maroc");                 forms.put("ville", "Rabat");
            forms.put("Type_du_contrat", "CDI");          forms.put("Experience", "4 ans");
            forms.put("Niveau_etudes", "Bac +5 et plus"); forms.put("Teletravail", "Hybride");

            System.out.println("******************");
            model model = new model(data,forms);
            System.out.println("Recommanded Posts  :");

            LinkedList<String> recommandedData = model.giveInstances(data);
            for(int i=0; i<recommandedData.size();i++) {
                System.out.println("Url: "+recommandedData.get(i).split(",")[2]);
            }
        }


}
