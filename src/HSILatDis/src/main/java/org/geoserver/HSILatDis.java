package org.geoserver;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.Hints;

@DescribeProcess(title = "HSI Latent Distribution", description = "HSI指数模型预测潜在分布")
public class HSILatDis implements GeoServerProcess {
    public HSILatDis() {}

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

    @DescribeResult(name = "outputGridCoverage", description = "输出潜在分布预测图")
    public GridCoverage2D execute(
            @DescribeParameter(name = "inputRaster1", description = "输入遥感影像", max = 1, min = 1)
                    GridCoverage2D raster1,
            @DescribeParameter(name = "inputRasterName1", description = "输入对应字段名", max = 1, min = 1)
                    String rasterName1,
            @DescribeParameter(name = "weight1", description = "输入权重值", max = 1, min = 1)
                    double weight1,
            @DescribeParameter(name = "inputRaster2", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster2,
            @DescribeParameter(name = "inputRasterName2", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName2,
            @DescribeParameter(name = "weight2", description = "输入权重值", max = 1, min = 0)
                    double weight2,
            @DescribeParameter(name = "inputRaster3", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster3,
            @DescribeParameter(name = "inputRasterName3", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName3,
            @DescribeParameter(name = "weight3", description = "输入权重值", max = 1, min = 0)
                    double weight3,
            @DescribeParameter(name = "inputRaster4", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster4,
            @DescribeParameter(name = "inputRasterName4", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName4,
            @DescribeParameter(name = "weight4", description = "输入权重值", max = 1, min = 0)
                    double weight4,
            @DescribeParameter(name = "inputRaster5", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster5,
            @DescribeParameter(name = "inputRasterName5", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName5,
            @DescribeParameter(name = "weight5", description = "输入权重值", max = 1, min = 0)
                    double weight5,
            @DescribeParameter(name = "inputRaster6", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster6,
            @DescribeParameter(name = "inputRasterName6", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName6,
            @DescribeParameter(name = "weight6", description = "输入权重值", max = 1, min = 0)
                    double weight6,
            @DescribeParameter(name = "inputRaster7", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster7,
            @DescribeParameter(name = "inputRasterName7", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName7,
            @DescribeParameter(name = "weight7", description = "输入权重值", max = 1, min = 0)
                    double weight7,
            @DescribeParameter(name = "inputRaster8", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster8,
            @DescribeParameter(name = "inputRasterName8", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName8,
            @DescribeParameter(name = "weight8", description = "输入权重值", max = 1, min = 0)
                    double weight8,
            @DescribeParameter(name = "inputRaster9", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster9,
            @DescribeParameter(name = "inputRasterName9", description = "输入对应字段名", max = 1, min = 0)
                    String rasterName9,
            @DescribeParameter(name = "weight9", description = "输入权重值", max = 1, min = 0)
                    double weight9,
            @DescribeParameter(name = "inputRaster10", description = "输入遥感影像", max = 1, min = 0)
                    GridCoverage2D raster10,
            @DescribeParameter(
                            name = "inputRasterName10",
                            description = "输入对应字段名",
                            max = 1,
                            min = 0)
                    String rasterName10,
            @DescribeParameter(name = "weight10", description = "输入权重值", max = 1, min = 0)
                    double weight10)
            throws IOException, InterruptedException {

        convertToTiff(raster1, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage1.tif");
        List<String> rasterNameList = new ArrayList();
        List<Double> weigthList = new ArrayList();
        rasterNameList.add(rasterName1);
        weigthList.add(weight1);
        if (raster2 != null) {
            convertToTiff(
                    raster2, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage2.tif");
            rasterNameList.add(rasterName2);
            weigthList.add(weight2);
        }
        if (raster3 != null) {
            convertToTiff(
                    raster3, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage3.tif");
            rasterNameList.add(rasterName3);
            weigthList.add(weight3);
        }

        if (raster4 != null) {
            convertToTiff(
                    raster4, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage4.tif");
            rasterNameList.add(rasterName4);
            weigthList.add(weight4);
        }
        if (raster5 != null) {
            convertToTiff(
                    raster5, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage5.tif");
            rasterNameList.add(rasterName5);
            weigthList.add(weight5);
        }

        if (raster6 != null) {
            convertToTiff(
                    raster6, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage6.tif");
            rasterNameList.add(rasterName6);
            weigthList.add(weight6);
        }

        if (raster7 != null) {
            convertToTiff(
                    raster7, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage7.tif");
            rasterNameList.add(rasterName7);
            weigthList.add(weight7);
        }
        if (raster8 != null) {
            convertToTiff(
                    raster8, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage8.tif");
            rasterNameList.add(rasterName8);
            weigthList.add(weight8);
        }

        if (raster9 != null) {
            convertToTiff(
                    raster9, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage9.tif");
            rasterNameList.add(rasterName9);
            weigthList.add(weight9);
        }

        if (raster10 != null) {
            convertToTiff(
                    raster10, "./src/main/webapp/data/python/HSI_Lat_Dis/inputGridCoverage10.tif");
            rasterNameList.add(rasterName10);
            weigthList.add(weight10);
        }
        // 构造 ProcessBuilder 对象
        ProcessBuilder pb =
                new ProcessBuilder(
                        "cmd.exe",
                        "/c",
                        "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat",
                        "./src/main/webapp/data/python/HSI_Lat_Dis.py");
        // 启动进程
        Process process = pb.start();
        // 获取进程的输出流
        OutputStreamWriter streamWriter = new OutputStreamWriter(process.getOutputStream());
        BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
        // 将 sourcePixelValue 发送给 Python
        for (int i = 0; i < rasterNameList.size(); i++) {
            String rasterName = rasterNameList.get(i);
            double weight = weigthList.get(i);
            bufferedWriter.write(rasterName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.write(String.valueOf(weight));
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        // 读取进程输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
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
            File file =
                    new File("./src/main/webapp/data/python/HSI_Lat_Dis/outputGridCoverage.tif");
            AbstractGridFormat format = GridFormatFinder.findFormat(file);
            Hints hints = null;
            if (format instanceof GeoTiffFormat) {
                hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            }
            GridCoverage2DReader fileReader = format.getReader(file, hints);
            GridCoverage2D ouputGridCoverage = (GridCoverage2D) fileReader.read(null);
            // 关闭资源
            fileReader.dispose(); // 添加这一行，确保资源已经释放
            // 删除文件
            if (file.exists()) {
                file.delete();
            }

            return ouputGridCoverage;

        } else {
            System.err.println("Python 脚本运行失败，错误码：" + exitCode);
        }
        return null;
    }
}
