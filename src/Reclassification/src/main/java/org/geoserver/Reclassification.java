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

@DescribeProcess(title = "Reclassification", description = "重分类")
public class Reclassification implements GeoServerProcess {
    public Reclassification() {}

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

    @DescribeResult(name = "outputGridCoverage", description = "输出重分类结果")
    public GridCoverage2D execute(
            @DescribeParameter(
                            name = "inputGridCoverage",
                            description = "输入待重分类影像",
                            max = 1,
                            min = 1)
                    GridCoverage2D inputGridCoverage,
            @DescribeParameter(name = "minValue1", description = "区间最小值1", max = 1, min = 0)
                    String minValue1,
            @DescribeParameter(name = "maxValue1", description = "区间最大值1", max = 1, min = 0)
                    String maxValue1,
            @DescribeParameter(name = "reclassValue1", description = "重分类值1", max = 1, min = 1)
                    String reclassValue1,
            @DescribeParameter(name = "minValue2", description = "区间最小值2", max = 1, min = 0)
                    String minValue2,
            @DescribeParameter(name = "maxValue2", description = "区间最大值2", max = 1, min = 0)
                    String maxValue2,
            @DescribeParameter(name = "reclassValue2", description = "重分类值2", max = 1, min = 0)
                    String reclassValue2,
            @DescribeParameter(name = "minValue3", description = "区间最小值3", max = 1, min = 0)
                    String minValue3,
            @DescribeParameter(name = "maxValue3", description = "区间最大值3", max = 1, min = 0)
                    String maxValue3,
            @DescribeParameter(name = "reclassValue3", description = "重分类值3", max = 1, min = 0)
                    String reclassValue3,
            @DescribeParameter(name = "minValue4", description = "区间最小值4", max = 1, min = 0)
                    String minValue4,
            @DescribeParameter(name = "maxValue4", description = "区间最大值4", max = 1, min = 0)
                    String maxValue4,
            @DescribeParameter(name = "reclassValue4", description = "重分类值4", max = 1, min = 0)
                    String reclassValue4,
            @DescribeParameter(name = "minValue5", description = "区间最小值5", max = 1, min = 0)
                    String minValue5,
            @DescribeParameter(name = "maxValue5", description = "区间最大值5", max = 1, min = 0)
                    String maxValue5,
            @DescribeParameter(name = "reclassValue5", description = "重分类值5", max = 1, min = 0)
                    String reclassValue5)
            throws Exception {
        try {
            convertToTiff(
                    inputGridCoverage,
                    "./src/main/webapp/data/python/reclassification/inputGridCoverage.tif");
            List<String> reclassificationList = new ArrayList();
            if (reclassValue1 != null) {
                reclassificationList.add(minValue1);
                reclassificationList.add(maxValue1);
                reclassificationList.add(reclassValue1);
            }
            if (reclassValue2 != null) {
                reclassificationList.add(minValue2);
                reclassificationList.add(maxValue2);
                reclassificationList.add(reclassValue2);
            }
            if (reclassValue3 != null) {
                reclassificationList.add(minValue3);
                reclassificationList.add(maxValue3);
                reclassificationList.add(reclassValue3);
            }
            if (reclassValue4 != null) {
                reclassificationList.add(minValue4);
                reclassificationList.add(maxValue4);
                reclassificationList.add(reclassValue4);
            }
            if (reclassValue5 != null) {
                reclassificationList.add(minValue5);
                reclassificationList.add(maxValue5);
                reclassificationList.add(reclassValue5);
            }
            // 构造 ProcessBuilder 对象
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "cmd.exe",
                            "/c",
                            "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat",
                            "./src/main/webapp/data/python/reclassification.py");
            // 启动进程
            Process process = pb.start();
            // 获取进程的输出流
            OutputStreamWriter streamWriter = new OutputStreamWriter(process.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
            //  发送给 Python
            bufferedWriter.write(Integer.toString(reclassificationList.size()));
            bufferedWriter.newLine();
            bufferedWriter.flush();
            for (String value : reclassificationList) {
                if (value != null) {
                    bufferedWriter.write(value);
                } else {
                    bufferedWriter.write("");
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            // 读取进程输出
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
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
                        new File(
                                "./src/main/webapp/data/python/reclassification/outputGridCoverage.tif");
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
