//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.geoserver;

import java.util.ArrayList;
import java.util.List;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.ParameterValueGroup;

@DescribeProcess(title = "RangeCoverageClip", description = "按范围裁剪栅格")
public class RangeCoverageClip implements GeoServerProcess {
    public RangeCoverageClip() {}

    @DescribeResult(name = "outputGridCoverage", description = "输出裁剪结果")
    public GridCoverage2D execute(
            @DescribeParameter(name = "inputGridCoverage", description = "输入待裁剪数据")
                    GridCoverage2D inputGridCoverage,
            @DescribeParameter(name = "clipBoundary", description = "输入裁剪范围")
                    SimpleFeatureCollection clipBoundary)
            throws Exception {
        FeatureIterator<SimpleFeature> iterator = clipBoundary.features();
        List<Geometry> all = new ArrayList<>();

        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                all.add(geometry);
            }
        } finally {
            if (iterator != null) {
                iterator.close();
            }
        }

        GridCoverage2D clippedCoverage = null;
        if (!all.isEmpty()) {
            // 获取裁剪范围的外接矩形
            GeometryFactory factory = JTSFactoryFinder.getGeometryFactory(null);
            Geometry[] geometries = all.toArray(new Geometry[0]);
            GeometryCollection boundary = new GeometryCollection(geometries, factory);
            Envelope envelope = boundary.getEnvelopeInternal();

            // 使用外接矩形进行栅格裁剪
            CoverageProcessor processor = new CoverageProcessor();
            ParameterValueGroup params = processor.getOperation("CoverageCrop").getParameters();
            params.parameter("Source").setValue(inputGridCoverage);
            ReferencedEnvelope referencedEnvelope =
                    new ReferencedEnvelope(
                            envelope, inputGridCoverage.getCoordinateReferenceSystem());
            params.parameter("ENVELOPE").setValue(referencedEnvelope);
            params.parameter("ForceMosaic").setValue(true);
            clippedCoverage = (GridCoverage2D) processor.doOperation(params);
        }

        return clippedCoverage;
    }
}
