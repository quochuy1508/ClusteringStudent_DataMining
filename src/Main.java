
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Record> records = getDataset();;
        Distance distance = new EuclideanDistance();
        int n = 1;
        int numCluster = 20;
        List<Double> sumOfSquaredErrors = new ArrayList<>();
        for (int k = 1; k <= numCluster; k++) {
            System.out.println("------------------------------"  + k  +   " CLUSTER "+ " -----------------------------------");
            int finalK = k;
            double avgMin = Double.MAX_VALUE;
            Map<Centroid, List<Record>> clustersTemp = null;
            for (int i = 0; i < n; i++) { // chay n lan voi tung k de toi uu giai phap voi k
                Map<Centroid, List<Record>> clusters = KMeans.fit(records, k, distance, 1000);
                StringBuffer temp = new StringBuffer("");
                final double[] avg = {0.0};
                if (clusters.size() != finalK) {
                    i--;
                    continue;
                }
                clusters.forEach((key, value) -> {
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
                    clustersTemp = clusters;
                    avgMin = avg[0]/k;
                    temp.append("TRUNG BÌNH CỦA TỪNG PHÂN LỚP: " + avgMin);
                    File path = new File("");
                    FileWriter csvWriter = new FileWriter(  path.getAbsoluteFile() + "/Result/" + finalK  +"Cluster" +".csv");
                    try {
                        csvWriter.append(temp);
                        csvWriter.flush();
                        csvWriter.close();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
            double sse = Errors.sse(clustersTemp, distance); // xac dinh so loi cua moi k
            sumOfSquaredErrors.add(sse);
        }

        // in ra tat ca cac loi cua trung k
        ListIterator<Double> it = sumOfSquaredErrors.listIterator();
        double[][] data = new double[2][numCluster];
        int temp = 0;
        // Traversing elements
        while(it.hasNext()){
            double sumError = it.next();
            data[0][temp] = temp + 1;
            data[1][temp] = sumError;
            temp++;
       }


        EblowChart chart = new EblowChart();
        chart.drawEblowChart(data);

    }

    private static List<Record> getDataset() throws IOException {
        String row;
        int iLoop = -1;
        List<Record> records = new ArrayList<>();
        File path = new File("");
        BufferedReader csvReader = new BufferedReader(new FileReader(path.getAbsolutePath() + "/Resourses/dataset.csv"));
        while ((row = csvReader.readLine()) != null) {
            iLoop++;
            if (iLoop % 8 != 7) continue;
            if(row.charAt(row.length()-1) == ',') {
                row = row + "0";
            }
            String[] data = row.split(",");
            for (int i = 0; i < data.length; i++) {
                if (data[i].equals("") || data[i].equals(" ")) {
                    data[i] = "0";
                }
            }
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
        }
        csvReader.close();
        return records;
    }
}