package org.geoserver;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.util.factory.Hints;

import javax.media.jai.PlanarImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.*;
import java.text.DecimalFormat;

@DescribeProcess(
            title = "CalcSlope",
            description = "坡度分析"
    )
    public class CalcSlope implements GeoServerProcess {
        public CalcSlope() {
        }
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

//    public float calcSlope(int cellX, int cellY, PlanarImage image,float zFactor) throws IOException {
//        DecimalFormat df = new DecimalFormat("#.0000");
//
//        Raster raster = image.getData();
//        int e = raster.getSample(cellX, cellY,0);
//        int e1 = raster.getSample(cellX - 1, cellY, 0);
//        int e2 = raster.getSample(cellX, cellY - 1, 0);
//        int e3 = raster.getSample(cellX + 1, cellY, 0);
//        int e4 = raster.getSample(cellX, cellY + 1, 0);
//        int e5 = raster.getSample(cellX - 1, cellY - 1, 0);
//        int e6 = raster.getSample(cellX + 1, cellY - 1, 0);
//        int e7 = raster.getSample(cellX + 1, cellY + 1, 0);
//        int e8 = raster.getSample(cellX - 1, cellY + 1, 0);
//
//        double slopeWE = ((e8 + 2 * e1 + e5) - (e7 + 2 * e3 + e6)) / (8 * 2041.823085) * zFactor; // 东西方向坡度
//        double slopeNW = ((e7 + 2 * e4 + e8) - (e6 + 2 * e2 + e5)) / (8 * 2041.823085) * zFactor; // 南北方向坡度
//        double slope = 100 * (Math.sqrt(Math.pow(slopeWE, 2) + Math.pow(slopeNW, 2)));
//
//        return Float.parseFloat(df.format(slope));
//    }
        @DescribeResult(name = "outputGridCoverage", description = "输出坡度分析结果")
        public GridCoverage2D execute(@DescribeParameter(name = "inputGridCoverage", description = "输入DEM高程影像") GridCoverage2D inputGridCoverage,
//
                                      @DescribeParameter(name = "zFactor", description = "z因子") float zFactor) throws Exception {

            //暂无法实现，报错：java.lang.ArrayIndexOutOfBoundsException: Coordinate out of bounds!
            //            RenderedImage sourceImage = inputGridCoverage.getRenderedImage();
//            int width = sourceImage.getWidth();
//            int height = sourceImage.getHeight();
//            PlanarImage image = (PlanarImage) inputGridCoverage.getRenderedImage();
//            float[][] destData = new float[height][width];;
//
//            for (int i = 1; i < height ; i++) {
//                for (int j = 1; j < width ; j++) {
//                    float slope = calcSlope(j, i, image,zFactor);
//                    destData[i-1][j-1] = slope;
//                }
//            }
//            // 创建输出GridCoverage2D对象
//            GridCoverageFactory coverageFactory = new GridCoverageFactory();
//            GridCoverage2D outputGridCoverage = coverageFactory.create("Slope", destData, inputGridCoverage.getEnvelope());
//
//            return outputGridCoverage;
            try {
                convertToTiff(inputGridCoverage, "./src/main/webapp/data/python/calc_slope/inputGridCoverage.tif");
                // 构造 ProcessBuilder 对象
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "D:\\Program Files (x86)\\QGIS\\bin\\python-qgis.bat", "./src/main/webapp/data/python/calc_Slope.py");
                // 启动进程
                Process process = pb.start();
                //获取进程的输出流
                OutputStreamWriter streamWriter = new OutputStreamWriter(process.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(streamWriter);
                // 将 sourcePixelValue 发送给 Python
                bufferedWriter.write(Float.toString(zFactor));
                bufferedWriter.newLine();
                bufferedWriter.flush();
                // 读取进程输出
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                // 读取进程错误输出
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.err.println(errorLine);
                }
                // 等待进程结束
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    System.out.println("Python 脚本运行成功！");
                    File file = new File("./src/main/webapp/data/python/calc_slope/outputGridCoverage.tif");
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
