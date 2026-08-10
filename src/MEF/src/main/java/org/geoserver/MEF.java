package org.geoserver;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.gce.arcgrid.ArcGridWriter;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Point;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

//import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@DescribeProcess(title="MEF", description="最大熵特征权重")
public class MEF implements GeoServerProcess {
    public MEF() {
    }
    public static void convertToAsc(GridCoverage2D gridCoverage, String outputFilePath) {
        try {
            // 创建 ArcGridWriter 实例
            File outputFile = new File(outputFilePath);
            ArcGridWriter writer = new ArcGridWriter(outputFile);

            // 设置输出的坐标参考系统
            writer.write(gridCoverage, null);

            // 关闭 ArcGridWriter
            writer.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String convertToCSV(SimpleFeatureCollection featureCollection, String filePath) throws IOException {
        String species="";
        // Create a FileWriter object to write the CSV file
        FileWriter writer = new FileWriter(new File(filePath));

        // Write the column names to the CSV file
        writer.write("species,lon,lat\n");

        // Write the feature data to the CSV file
        FeatureIterator<SimpleFeature> features = featureCollection.features();
        while (features.hasNext()) {
            SimpleFeature feature = features.next();
            species = feature.getID().replaceAll("\\.\\d+$", "");
            // Extract the Point geometry from the feature
            Point point = (Point) feature.getDefaultGeometry();

            // Extract the longitude and latitude from the Point geometry
            double lon = point.getX();
            double lat = point.getY();

            // Write the feature data to the CSV file
            writer.write(species + "," + lon + "," + lat + "\n");
        }

        // Close the FileWriter object
        writer.close();
        return species;
    }
    public static String CSVParser(File file){
        StringBuilder result = new StringBuilder();
        result.append("各环境因子的权重:\n");
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] header = reader.readNext(); // 读取CSV文件的表头
            String[] line = reader.readNext();
            // 遍历表头，找到包含"contribution"的列
            for (int i = 0; i < header.length; i++) {
                if (header[i].contains("contribution")) {
                    String columnName = header[i];
                    if (line != null && line.length > i && !line[i].isEmpty()) {
                        String value = line[i];
                        result.append(columnName.split(" ")[0]).append(": ").append(value).append("%\n");
                    } else {
                        System.out.println("Warning: Skipping sample with missing environmental data.");

                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    @DescribeResult(name="result", description="输出最大熵特征权重")
    public String execute(       @DescribeParameter(name = "inputFeatures",description = "输入活动点位数据",max = 1,min = 1) SimpleFeatureCollection inputFeatures,
                                         @DescribeParameter(name = "inputRaster1",description = "输入遥感影像",max = 1,min = 1) GridCoverage2D raster1,
                                         @DescribeParameter(name = "inputRasterName1",description = "输入对应字段名",max = 1,min = 1) String rasterName1,
                                         @DescribeParameter(name = "inputRaster2",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster2,
                                         @DescribeParameter(name = "inputRasterName2",description = "输入对应字段名",max = 1,min = 0) String rasterName2,
                                         @DescribeParameter(name = "inputRaster3",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster3,
                                         @DescribeParameter(name = "inputRasterName3",description = "输入对应字段名",max = 1,min = 0) String rasterName3,
                                         @DescribeParameter(name = "inputRaster4",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster4,
                                         @DescribeParameter(name = "inputRasterName4",description = "输入对应字段名",max = 1,min = 0) String rasterName4,
                                         @DescribeParameter(name = "inputRaster5",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster5,
                                         @DescribeParameter(name = "inputRasterName5",description = "输入对应字段名",max = 1,min = 0) String rasterName5,
                                         @DescribeParameter(name = "inputRaster6",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster6,
                                         @DescribeParameter(name = "inputRasterName6",description = "输入对应字段名",max = 1,min = 0) String rasterName6,
                                         @DescribeParameter(name = "inputRaster7",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster7,
                                         @DescribeParameter(name = "inputRasterName7",description = "输入对应字段名",max = 1,min = 0) String rasterName7,
                                         @DescribeParameter(name = "inputRaster8",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster8,
                                         @DescribeParameter(name = "inputRasterName8",description = "输入对应字段名",max = 1,min = 0) String rasterName8,
                                         @DescribeParameter(name = "inputRaster9",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster9,
                                         @DescribeParameter(name = "inputRasterName9",description = "输入对应字段名",max = 1,min = 0) String rasterName9,
                                         @DescribeParameter(name = "inputRaster10",description = "输入遥感影像",max = 1,min = 0) GridCoverage2D raster10,
                                         @DescribeParameter(name = "inputRasterName10",description = "输入对应字段名",max = 1,min = 0) String rasterName10) throws IOException, InterruptedException {
        String folderPath = "./src/main/webapp/data/python/MEF/raster";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            System.out.println("The folder does not exist.");
        }

        if (!folder.isDirectory()) {
            System.out.println("The specified path is not a folder.");
        }
        File[] files = folder.listFiles();
        // 遍历并删除文件
        for (File file : files) {
            file.delete();
        }
        String species=convertToCSV(inputFeatures,"./src/main/webapp/data/python/MEF/inputFeatures.csv");
        convertToAsc(raster1, "./src/main/webapp/data/python/MEF/raster/"+rasterName1+".asc");
        List<String> rasterNameList = new ArrayList();
        if (raster2 != null) {
            convertToAsc(raster2, "./src/main/webapp/data/python/MEF/raster/"+rasterName2+".asc");
        }
        if (raster3 != null) {
            convertToAsc(raster3, "./src/main/webapp/data/python/MEF/raster/"+rasterName3+".asc");

        }

        if (raster4 != null) {
            convertToAsc(raster4, "./src/main/webapp/data/python/MEF/raster/"+rasterName4+".asc");

        }
        if (raster5 != null) {
            convertToAsc(raster5, "./src/main/webapp/data/python/MEF/raster/"+rasterName5+".asc");

        }

        if (raster6 != null) {
            convertToAsc(raster6, "./src/main/webapp/data/python/MEF/raster/"+rasterName6+".asc");

        }

        if (raster7 != null) {
            convertToAsc(raster7, "./src/main/webapp/data/python/MEF/raster/"+rasterName7+".asc");

        }
        if (raster8 != null) {
            convertToAsc(raster8, "./src/main/webapp/data/python/MEF/raster/"+rasterName8+".asc");

        }

        if (raster9 != null) {
            convertToAsc(raster9, "./src/main/webapp/data/python/MEF/raster/"+rasterName9+".asc");

        }

        if (raster10 != null) {
            convertToAsc(raster10, "./src/main/webapp/data/python/MEF/raster/"+rasterName10+".asc");

        }
        String sourcePath = "./src/main/webapp/data/python/MEF/raster/"+rasterName1+".prj";
        String destinationPath = "./src/main/webapp/data/python/MEF/result/"+species+".prj";
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.deleteIfExists(destination);
        Files.copy(source, destination);

        String outputformat = "logistic";
        String outputdirectory = "./src/main/webapp/data/python/MEF/result";
        String samplesfile = "./src/main/webapp/data/python/MEF/inputFeatures.csv";
        String environmentallayers = "./src/main/webapp/data/python/MEF/raster";
        String randomtestpoints = "10";
        String replicatetype="bootstrap";
        String autorun = "true";
        String warnings = "false";
        String skipifexists="false";
        String askoverwrite="false";
        String visible="false";
        // 构建命令行参数
        String[] command = {
                "java",
                "-Dfile.encoding=GBK",
                "-jar",
                "./src/main/webapp/data/python/3.4.1/maxent.jar",
                "outputformat=" + outputformat,
                "outputdirectory=" + outputdirectory,
                "samplesfile=" + samplesfile,
                "environmentallayers=" + environmentallayers,
                "randomtestpoints=" + randomtestpoints,
                "replicatetype="+replicatetype,
                "autorun=" + autorun,
                "warnings=" + warnings,
                "skipifexists=" + skipifexists,
                "askoverwrite=" + askoverwrite,
                "visible=" + visible
        };

        try {
            // 执行命令行命令
            Process process = Runtime.getRuntime().exec(command);
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                // 执行成功，执行后续操作
                System.out.println("命令执行成功");
                if(Files.exists(destination)) {
                    File file = new File("./src/main/webapp/data/python/MEF/result/maxentResults.csv");
                    String result=CSVParser(file);
                    return  result;
                }
            } else {
                System.out.println("命令执行失败，错误码：" + exitValue);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }




}
