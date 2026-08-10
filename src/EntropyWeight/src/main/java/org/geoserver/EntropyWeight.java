package org.geoserver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;

@DescribeProcess(title = "EntropyWeight", description = "熵权法确定因子权重")
public class EntropyWeight implements GeoServerProcess {
    public EntropyWeight() {}

    public static void convertToTiff(GridCoverage2D gridCoverage, String outputFilePath) {
        try {
            // 创建 GeoTiffWriter 实例
            File outputFile = new File(outputFilePath);
            GeoTiffWriter writer = new GeoTiffWriter(outputFile);

            // 设置输出的坐标参考系统
            writer.write(gridCoverage, null);

            // 关闭 GeoTiffWriter
            writer.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DescribeResult(name = "result", description = "输出熵权法评估结果")
    public String execute(
            @DescribeParameter(name = "inputRaster1", description = "输入遥感影像", max = 1, min = 1)
                    GridCoverage2D raster1,
            @DescribeParameter(name = "inputRasterName1", description = "输入对应字段名", max = 1, min = 1)
                    String rasterName1,
            @DescribeParameter(name = "inputRaster2", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster2,
            @DescribeParameter(name = "inputRasterName2", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName2,
            @DescribeParameter(name = "inputRaster3", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster3,
            @DescribeParameter(name = "inputRasterName3", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName3,
            @DescribeParameter(name = "inputRaster4", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster4,
            @DescribeParameter(name = "inputRasterName4", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName4,
            @DescribeParameter(name = "inputRaster5", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster5,
            @DescribeParameter(name = "inputRasterName5", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName5,
            @DescribeParameter(name = "inputRaster6", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster6,
            @DescribeParameter(name = "inputRasterName6", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName6,
            @DescribeParameter(name = "inputRaster7", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster7,
            @DescribeParameter(name = "inputRasterName7", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName7,
            @DescribeParameter(name = "inputRaster8", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster8,
            @DescribeParameter(name = "inputRasterName8", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName8,
            @DescribeParameter(name = "inputRaster9", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster9,
            @DescribeParameter(name = "inputRasterName9", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName9,
            @DescribeParameter(name = "inputRaster10", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster10,
            @DescribeParameter(
                            name = "inputRasterName10",
                            description = "输入对应字段名",
                            max = 1,
                            min = 0)
                    String rasterName10)
            throws IOException, InterruptedException {

        convertToTiff(
                raster1, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage1.tif");
        List<String> rasterNameList = new ArrayList();
        rasterNameList.add(rasterName1);
        if (raster2 != null) {
            convertToTiff(
                    raster2, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage2.tif");
            rasterNameList.add(rasterName2);
        }
        if (raster3 != null) {
            convertToTiff(
                    raster3, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage3.tif");
            rasterNameList.add(rasterName3);
        }

        if (raster4 != null) {
            convertToTiff(
                    raster4, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage4.tif");
            rasterNameList.add(rasterName4);
        }
        if (raster5 != null) {
            convertToTiff(
                    raster5, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage5.tif");
            rasterNameList.add(rasterName5);
        }

        if (raster6 != null) {
            convertToTiff(
                    raster6, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage6.tif");
            rasterNameList.add(rasterName6);
        }

        if (raster7 != null) {
            convertToTiff(
                    raster7, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage7.tif");
            rasterNameList.add(rasterName7);
        }
        if (raster8 != null) {
            convertToTiff(
                    raster8, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage8.tif");
            rasterNameList.add(rasterName8);
        }

        if (raster9 != null) {
            convertToTiff(
                    raster9, "./src/main/webapp/data/python/entropy_weight/inputGridCoverage9.tif");
            rasterNameList.add(rasterName9);
        }

        if (raster10 != null) {
            convertToTiff(
                    raster10,
                    "./src/main/webapp/data/python/entropy_weight/inputGridCoverage10.tif");
            rasterNameList.add(rasterName10);
        }
        // 构造 ProcessBuilder 对象
        ProcessBuilder pb =
                new ProcessBuilder(
                        "cmd.exe",
                        "/c",
                        "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat",
                        "./src/main/webapp/data/python/entropy_weight.py");
        // 启动进程
        Process process = pb.start();
        // 获取进程的输出流
        OutputStreamWriter streamWriter = new OutputStreamWriter(process.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
        // 将 sourcePixelValue 发送给 Python
        for (String rasterName : rasterNameList) {
            bufferedWriter.write(rasterName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        // 关闭发送给 Python 的输入流
        bufferedWriter.close();
        // 读取进程输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder outputBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            outputBuilder.append(line).append("\n");
        }
        // 关闭进程输出流
        reader.close();
        // 读取进程错误输出
        BufferedReader errorReader =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            System.err.println(errorLine);
        }
        // 等待进程结束
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            System.out.println("Python 脚本运行成功！");
            String output = outputBuilder.toString();
            return output;
        } else {
            System.err.println("Python 脚本运行失败，错误码：" + exitCode);
        }
        return null;
    }
}
