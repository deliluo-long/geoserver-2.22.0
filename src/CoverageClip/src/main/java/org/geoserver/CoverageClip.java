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
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.parameter.ParameterValueGroup;

@DescribeProcess(
        title = "CoverageClip",
        description = "掩膜提取"
)
public class CoverageClip implements GeoServerProcess {
    public CoverageClip() {
    }

    @DescribeResult(
            name = "outputGridCoverage",
            description = "输出裁剪结果"
    )
    public GridCoverage2D execute(@DescribeParameter(name = "inputGridCoverage",description = "输入待裁剪数据") GridCoverage2D inputGridCoverage, @DescribeParameter(name = "clipBoundry",description = "输入裁剪范围") SimpleFeatureCollection clipBoundry) throws Exception {
        FeatureIterator<SimpleFeature> iterator = clipBoundry.features();
        List<Geometry> all = new ArrayList();

        try {
            while(iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature)iterator.next();
                Geometry geometry = (Geometry)feature.getDefaultGeometry();
                all.add(geometry);
            }
        } finally {
            if (iterator != null) {
                iterator.close();
            }

        }

        GridCoverage2D clippedCoverage = null;
        if (all.size() > 0) {
            CoverageProcessor processor = new CoverageProcessor();
            ParameterValueGroup params = processor.getOperation("CoverageCrop").getParameters();
            params.parameter("Source").setValue(inputGridCoverage);
            GeometryFactory factory = JTSFactoryFinder.getGeometryFactory((Hints)null);
            Geometry[] a = (Geometry[])all.toArray(new Geometry[0]);
            GeometryCollection c = new GeometryCollection(a, factory);
            Envelope envelope = ((Geometry)all.get(0)).getEnvelopeInternal();
            double x1 = envelope.getMinX();
            double y1 = envelope.getMinY();
            double x2 = envelope.getMaxX();
            double y2 = envelope.getMaxY();
            ReferencedEnvelope referencedEnvelope = new ReferencedEnvelope(x1, x2, y1, y2, inputGridCoverage.getCoordinateReferenceSystem());
            params.parameter("ENVELOPE").setValue(referencedEnvelope);
            params.parameter("ROI").setValue(c);
            params.parameter("ForceMosaic").setValue(true);
            clippedCoverage = (GridCoverage2D)processor.doOperation(params);
        }

        return clippedCoverage;
    }
}

