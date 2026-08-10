package org.geoserver;

import java.io.*;
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

@DescribeProcess(title = "CalcSlope", description = "坡度分析")
public class CalcSlope implements GeoServerProcess {
    public CalcSlope() {}

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

    @DescribeResult(name = "outputGridCoverage", description = "输出坡度分析结果")
    public GridCoverage2D execute(
            @DescribeParameter(name = "inputGridCoverage", description = "输入DEM高程影像")
                    GridCoverage2D inputGridCoverage,
            //
            @DescribeParameter(name = "zFactor", description = "z因子") float zFactor)
            throws Exception {
        try {
            convertToTiff(
                    inputGridCoverage,
                    "./src/main/webapp/data/python/calc_slope/inputGridCoverage.tif");
            // 构造 ProcessBuilder 对象
            ProcessBuilder pb =
                    new ProcessBuilder(
                            "cmd.exe",
                            "/c",
                            "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat",
                            "./src/main/webapp/data/python/calc_Slope.py");
            // 启动进程
            Process process = pb.start();
            // 获取进程的输出流
            OutputStreamWriter streamWriter = new OutputStreamWriter(process.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
            bufferedWriter.write(Float.toString(zFactor));
            bufferedWriter.newLine();
            bufferedWriter.flush();
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
                        new File("./src/main/webapp/data/python/calc_slope/outputGridCoverage.tif");
                AbstractGridFormat format = GridFormatFinder.findFormat(file);
                Hints hints = null;
                if (format instanceof GeoTiffFormat) {
                    hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
                }
                GridCoverage2DReader fileReader = format.getReader(file, hints);
                GridCoverage2D ouputGridCoverage = (GridCoverage2D) fileReader.read(null);
                // 关闭资源
                fileReader.dispose();
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
