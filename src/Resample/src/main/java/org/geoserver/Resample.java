package org.geoserver;

import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.Envelope2D;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@DescribeProcess(
        title = "Resample",
        description = "重采样"
)
public class Resample implements GeoServerProcess {
    public Resample() {
    }


    @DescribeResult(name = "outputGridCoverage", description = "输出重采样结果")
    public GridCoverage2D execute(@DescribeParameter(name = "inputGridCoverage", description = "输入待重采样影像") GridCoverage2D inputGridCoverage,
                                  @DescribeParameter(name = "targetGridCoverage", description = "输入参照标准影像") GridCoverage2D  targetGridCoverage ) throws Exception {
        try {
            // 获取源和目标投影的坐标参考系统
            GridGeometry2D geometry = targetGridCoverage.getGridGeometry();
            CoordinateReferenceSystem targetCRSObj = geometry.getCoordinateReferenceSystem();
            Envelope2D envelope = targetGridCoverage.getEnvelope2D();
            RenderedImage image=targetGridCoverage.getRenderedImage();
            int numCols=image.getWidth();
            int numRows=image.getHeight();
            // 进行重采样
            GridEnvelope2D transrange = new GridEnvelope2D(0, 0, numCols, numRows);
            GridGeometry2D transgeomtry = new GridGeometry2D((GridEnvelope) transrange, envelope);
            GridCoverage2D reprojectedCoverage = (GridCoverage2D) Operations.DEFAULT.resample(inputGridCoverage, targetCRSObj, (GridGeometry) transgeomtry, null);

            return reprojectedCoverage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
