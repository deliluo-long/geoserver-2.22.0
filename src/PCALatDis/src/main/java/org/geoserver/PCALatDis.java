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

@DescribeProcess(title = "PCA Latent Distribution", description = "主成分分析法预测潜在分布")
public class PCALatDis implements GeoServerProcess {
    public PCALatDis() {}

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

        convertToTiff(raster1, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage1.tif");
        List<String> rasterNameList = new ArrayList();
        rasterNameList.add(rasterName1);
        if (raster2 != null) {
            convertToTiff(
                    raster2, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage2.tif");
            rasterNameList.add(rasterName2);
        }
        if (raster3 != null) {
            convertToTiff(
                    raster3, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage3.tif");
            rasterNameList.add(rasterName3);
        }

        if (raster4 != null) {
            convertToTiff(
                    raster4, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage4.tif");
            rasterNameList.add(rasterName4);
        }
        if (raster5 != null) {
            convertToTiff(
                    raster5, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage5.tif");
            rasterNameList.add(rasterName5);
        }

        if (raster6 != null) {
            convertToTiff(
                    raster6, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage6.tif");
            rasterNameList.add(rasterName6);
        }

        if (raster7 != null) {
            convertToTiff(
                    raster7, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage7.tif");
            rasterNameList.add(rasterName7);
        }
        if (raster8 != null) {
            convertToTiff(
                    raster8, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage8.tif");
            rasterNameList.add(rasterName8);
        }

        if (raster9 != null) {
            convertToTiff(
                    raster9, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage9.tif");
            rasterNameList.add(rasterName9);
        }

        if (raster10 != null) {
            convertToTiff(
                    raster10, "./src/main/webapp/data/python/PCA_Lat_Dis/inputGridCoverage10.tif");
            rasterNameList.add(rasterName10);
        }
        // 构造 ProcessBuilder 对象
        ProcessBuilder pb =
                new ProcessBuilder(
                        "cmd.exe",
                        "/c",
                        "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat",
                        "./src/main/webapp/data/python/PCA_Lat_Dis.py");
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
        // 读取进程输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.err.println(line);
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
            //            int width = raster1.getRenderedImage().getWidth();
            //            int height = raster1.getRenderedImage().getHeight();
            //            // 将List<Float>转换为float[][]
            //            float[][] array = new float[height][width];
            //            int index = 0;
            //            for (int i = 0; i < height; i++) {
            //                for (int j = 0; j < width; j++) {
            //                    array[i][j] = scores.get(index++);
            //                }
            //            }
            //            GridCoverageFactory factory=new GridCoverageFactory();
            //            GridCoverage2D
            // ouputGridCoverage=factory.create("outputGridCoverage",array,raster1.getEnvelope());
            File file =
                    new File("./src/main/webapp/data/python/PCA_Lat_Dis/outputGridCoverage.tif");
            AbstractGridFormat format = GridFormatFinder.findFormat(file);
            Hints hints = null;
            if (format instanceof GeoTiffFormat) {
                hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
            }
            GridCoverage2DReader fileReader = format.getReader(file, hints);
            GridCoverage2D ouputGridCoverage = (GridCoverage2D) fileReader.read(null);
            // 关闭资源
            fileReader.dispose();
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
