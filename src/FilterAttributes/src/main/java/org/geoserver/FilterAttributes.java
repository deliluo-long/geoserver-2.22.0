//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.geoserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geoserver.wps.gs.GeoServerProcess;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

@DescribeProcess(
        title = "FilterAttributes",
        description = "矢量属性有效数据筛选"
)
public class FilterAttributes implements GeoServerProcess {
    public FilterAttributes() {
    }

    @DescribeResult(
            name = "outputFeatures",
            description = "输出筛选数据"
    )
    public SimpleFeatureCollection execute(@DescribeParameter(name = "inputFeatures",description = "输入待筛选数据") SimpleFeatureCollection inputFeatures,
                                           @DescribeParameter(name = "percentage",description = "输入百分比阈值") int percentage) throws Exception {
        int numAttributes = ((SimpleFeatureType)inputFeatures.getSchema()).getAttributeCount();
        int[] validCounts = new int[numAttributes];
        SimpleFeatureIterator it = inputFeatures.features();
        Throwable var6 = null;

        try {
            while(it.hasNext()) {
                SimpleFeature feature = (SimpleFeature)it.next();

                for(int i = 0; i < numAttributes; ++i) {
                    Object value = feature.getAttribute(i);
                    if (value != null && (!(value instanceof Number) || ((Number)value).doubleValue() != 0.0)) {
                        int var10002 = validCounts[i]++;
                    }
                }
            }
        } catch (Throwable var38) {
            var6 = var38;
            throw var38;
        } finally {
            if (it != null) {
                if (var6 != null) {
                    try {
                        it.close();
                    } catch (Throwable var34) {
                        var6.addSuppressed(var34);
                    }
                } else {
                    it.close();
                }
            }

        }

        SimpleFeatureType inputSchema = (SimpleFeatureType)inputFeatures.getSchema();
        SimpleFeatureTypeBuilder schemaBuilder = new SimpleFeatureTypeBuilder();
        schemaBuilder.setName(inputSchema.getName());
        List<AttributeDescriptor> attributeDescriptors = new ArrayList();
        List<Integer> retainIndices = new ArrayList();
        System.out.println(numAttributes);
        int minValidCount= (inputFeatures.size()* percentage) / 100;;
        for(int i = 0; i < numAttributes; ++i) {
            if (validCounts[i] >= minValidCount) {
                retainIndices.add(i);
                AttributeDescriptor descriptor = inputSchema.getDescriptor(i);
                attributeDescriptors.add(descriptor);
            }
        }

        schemaBuilder.setName("PointfilterAttributes");
        schemaBuilder.addAll(attributeDescriptors);
        SimpleFeatureType newSchema = schemaBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(newSchema);
        System.out.println(newSchema);
        System.out.println(retainIndices);
        List<SimpleFeature> features = new ArrayList();
        it = inputFeatures.features();
        Throwable var13 = null;

        try {
            while(it.hasNext()) {
                SimpleFeature inputFeature = (SimpleFeature)it.next();
                featureBuilder.reset();
                Iterator var15 = retainIndices.iterator();

                while(var15.hasNext()) {
                    Integer i = (Integer)var15.next();
                    featureBuilder.add(inputFeature.getAttribute(i));
                }

                SimpleFeature newFeature = featureBuilder.buildFeature((String)null);
                features.add(newFeature);
                System.out.println(newFeature.getAttributes());
            }
        } catch (Throwable var36) {
            var13 = var36;
            throw var36;
        } finally {
            if (it != null) {
                if (var13 != null) {
                    try {
                        it.close();
                    } catch (Throwable var35) {
                        var13.addSuppressed(var35);
                    }
                } else {
                    it.close();
                }
            }

        }

        return DataUtilities.collection(features);
    }
}
