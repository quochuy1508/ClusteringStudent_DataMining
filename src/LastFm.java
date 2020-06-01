import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.stream.Collectors.toSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LastFm {

    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        List<Record> records = getDataset();;
//        ListIterator<Record> iterator = records.listIterator();
//        System.out.println("\nUsing ListIterator:\n");
//        while (iterator.hasNext()) {
//            System.out.println("Value is : "
//                    + iterator.next());
//        }

//        List<Record> records = // the dataset;
                Distance distance = new EuclideanDistance();
        List<Double> sumOfSquaredErrors = new ArrayList<>();
        for (int k = 2; k <= 16; k++) {
            System.out.println("------------------------------ CLUSTER " + k + " -----------------------------------");
            Map<Centroid, List<Record>> clusters = KMeans.fit(records, k, distance, 1000);
            clusters.forEach((key, value) -> {
            System.out.println("------------------------------ ok -----------------------------------");
            System.out.println(sortedCentroid(key));
            String members = String.join(", ", value
                    .stream()
                    .map(Record::getDescription)
                    .collect(toSet()));
            System.out.print(members);
            System.out.println();
            System.out.println();
        });
            double sse = Errors.sse(clusters, distance);
            sumOfSquaredErrors.add(sse);
        }
        ListIterator<Double> iterator = sumOfSquaredErrors.listIterator();
        System.out.println("\nUsing ListIterator:\n");
        while (iterator.hasNext()) {
            System.out.println("Value is : "
                    + iterator.next());
        }

//        Distance distance = new EuclideanDistance();
//        List<Double> sumOfSquaredErrors = new ArrayList<>();
//        for (int k = 2; k <= 16; k++) {
//            Map<Centroid, List<Record>> clusters = KMeans.fit(records, k, distance, 1000);
//            Double sse = Errors.sse(clusters, distance);
//            sumOfSquaredErrors.add(sse);
//        }
//        Map<Centroid, List<Record>> clusters = KMeans.fit(records, 3, new EuclideanDistance(), 1000);
//        clusters.forEach((key, value) -> {
//            System.out.println("------------------------------ CLUSTER -----------------------------------");
//
//            System.out.println(sortedCentroid(key));
//            String members = String.join(", ", value
//                    .stream()
//                    .map(Record::getDescription)
//                    .collect(toSet()));
//            System.out.print(members);
//            System.out.println();
//            System.out.println();
//        });

//        Map<String, Object> json = convertToD3CompatibleMap(clusters);
//        System.out.println(mapper.writeValueAsString(json));
    }

    private static Map<String, Object> convertToD3CompatibleMap(Map<Centroid, List<Record>> clusters) {
        Map<String, Object> json = new HashMap<>();
        json.put("name", "Musicians");
        List<Map<String, Object>> children = new ArrayList<>();
        clusters.forEach((key, value) -> {
            Map<String, Object> child = new HashMap<>();
            child.put("name", dominantGenre(sortedCentroid(key)));
            List<Map<String, String>> nested = new ArrayList<>();
//            for (Record record : value) {
//                nested.add(Collections.singletonMap("name", record.getDescription()));
//            }
            child.put("children", nested);

            children.add(child);
        });
        json.put("children", children);
        return json;
    }

    private static String dominantGenre(Centroid centroid) {
        return centroid
                .getCoordinates()
                .keySet()
                .stream()
                .limit(2)
                .collect(Collectors.joining(", "));
    }

    private static Centroid sortedCentroid(Centroid key) {
        List<Map.Entry<String, Double>> entries = new ArrayList<>(key
                .getCoordinates()
                .entrySet());
        entries.sort((e1, e2) -> e2
                .getValue()
                .compareTo(e1.getValue()));

        Map<String, Double> sorted = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : entries) {
            sorted.put(entry.getKey(), entry.getValue());
        }

        return new Centroid(sorted);
    }

    private static List<Record> getDataset() throws IOException {
        String pathToCsv = "/home/quochuy/Desktop/TAI_LIEU/ML&DM/student-performance-dataset/Student_Performance_Data_Wide_Version.csv";
        String row;
        int iLoop = -1;
        List<Record> records = new ArrayList<>();

        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        while ((row = csvReader.readLine()) != null) {
            iLoop++;
            if (iLoop % 8 != 0) continue;
            if(row.charAt(row.length()-1) == ',') {
                row = row + "0";
            }
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                if (data[i].equals("") || data[i].equals(" ")) {
                    data[i] = "0";
                }
//                System.out.print(data[i] + " ");
            }
//            System.out.println();
            String description = data[0];
            Map<String, Double> features = new HashMap< String,Double>();;
            features.put("Exam1", Double.parseDouble(data[2]));
            features.put("Exam2", Double.parseDouble(data[3]));
            features.put("Exam3", Double.parseDouble(data[4]));
            features.put("Exam4", Double.parseDouble(data[5]));
            features.put("Exam5", Double.parseDouble(data[6]));
            features.put("Exam6", Double.parseDouble(data[7]));
            features.put("Exam7", Double.parseDouble(data[8]));
            records.add(new Record(description, features));
            // do something with the data
        }
        csvReader.close();
        return records;
    }
}