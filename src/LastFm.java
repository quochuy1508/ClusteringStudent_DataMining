import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class LastFm {

    private static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        List<Record> records = getDataset();;
        System.out.println("ALL COUNT RECORD: " + records.size());
        Distance distance = new EuclideanDistance();
        List<Double> sumOfSquaredErrors = new ArrayList<>();
        double max = Double.MAX_VALUE;
        for (int k = 2; k <= 7; k++) {
            System.out.println("------------------------------ CLUSTER " + k + " -----------------------------------");
            int finalK = k;
            double avgMin = Double.MAX_VALUE;
            for (int i = 0; i < 50; i++) { // chay 50 lan voi tung k de chay toi uu
                Map<Centroid, List<Record>> clusters = KMeans.fit(records, k, distance, 1000);
                StringBuffer temp = new StringBuffer("");
                final double[] avg = {0.0};
                if (clusters.size() != finalK) {
                    i--;
                    continue;
                }
                clusters.forEach((key, value) -> {


//                    System.out.println("key: " + key);
//                    temp[0] += "\n======================================================================\n";
//                    temp[0] += "Centroid: " + key.toString() +"\n";
//                    temp[0] += "Name             Distance         \n";
                    temp.append("\n======================================================================\n");
                    temp.append("Điểm Trung Tâm: " + key.toString() +"\n");
                    temp.append("MSSV             KHOẢNG CÁCH TỚI TT         \n");
                    double avenger = 0;
                    for (int v = 0; v < value.size(); v++) { // tong khoang cach cua tung cum toi tam cua no
                        double currentDistance = distance.calculate(value.get(v).getFeatures(), key.getCoordinates());
                        avenger += currentDistance;
                        temp.append(value.get(v).getDescription() + "     ");
                        temp.append(currentDistance + "     \n");
                    }
                    avg[0] += (avenger/value.size());
                });
                if (avgMin > (avg[0]/k)) {
                    double sse = Errors.sse(clusters, distance);
                    sumOfSquaredErrors.add(sse);
                    avgMin = avg[0]/k;
                    temp.append("TRUNG BÌNH CỦA TỪNG PHÂN LỚP: " + avgMin);
                    File path = new File("");
                    FileWriter csvWriter = new FileWriter(  path.getAbsoluteFile() + "/Result/Cluster" + finalK +".csv");
                    try {
                        csvWriter.append(temp);
                        csvWriter.flush();
                        csvWriter.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
//            Map<String, Object> json = convertToD3CompatibleMap(clusters);
//            System.out.println(mapper.writeValueAsString(json));
        }
    }

    private static Map<String, Object> convertToD3CompatibleMap(Map<Centroid, List<Record>> clusters) {
        Map<String, Object> json = new HashMap<>();
        json.put("name", "Student");
        List<Map<String, Object>> children = new ArrayList<>();
        clusters.forEach((key, value) -> {
            Map<String, Object> child = new HashMap<>();
            child.put("name", dominantGenre(sortedCentroid(key)));
            List<Map<String, String>> nested = new ArrayList<>();
            for (Record record : value) {
                nested.add(Collections.singletonMap("name", record.getDescription()));
            }
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
        String row;
        int iLoop = -1;
        List<Record> records = new ArrayList<>();
        File path = new File("");
        BufferedReader csvReader = new BufferedReader(new FileReader(path.getAbsolutePath() + "/Resourses/dataset.csv"));
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