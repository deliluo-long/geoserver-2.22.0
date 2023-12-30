package org.geoserver;

import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.processing.Operations;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.io.*;


@DescribeProcess(
        title = "Reprojection",
        description = "重投影"
)
public class Reprojection implements GeoServerProcess {
    public Reprojection() {
    }


    @DescribeResult(name = "outputGridCoverage", description = "输出重投影结果")
    public GridCoverage2D execute(@DescribeParameter(name = "inputGridCoverage", description = "输入待重投影影像") GridCoverage2D inputGridCoverage,
//                                  @DescribeParameter(name = "sourceCRS", description = "输入源投影CRS") String sourceCRS,
                                  @DescribeParameter(name = "targetCRS", description = "输入目标投影CRS") String targetCRS) throws Exception {
        try {
            // 获取源和目标投影的坐标参考系统
            CoordinateReferenceSystem targetCRSObj = CRS.decode(targetCRS);

            // 进行重投影
            GridCoverage2D reprojectedCoverage = (GridCoverage2D)  Operations.DEFAULT.resample(inputGridCoverage, targetCRSObj);

            return reprojectedCoverage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
