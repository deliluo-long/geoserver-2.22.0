# GeoServer 2.22.0 —— 自定义 WPS 处理过程代码层（Custom WPS Process Layer）

> 本仓库基于 **GeoServer 2.22.0** 源码，在其 OGC WPS（Web Processing Service）框架之上，新增了一套**自定义地理处理过程代码层（Custom WPS Process Layer）**，包含 **16 个相互独立、可通过 WPS 直接调用的空间分析算法模块**。
>
> ⚠️ 说明：GeoServer 上游本身已实现 OGC WPS 标准（见下方上游 README）。本仓库的增量工作是：**在 GeoServer 既有的 WPS 扩展之上，新增了 16 个自定义 WPS 处理过程（Process）**，并注册进 GeoServer 使其对外暴露，而非重新实现 WPS 协议本身。

---

## 一、本项目核心工作：自定义 WPS 代码层

### 1.1 做了什么

在 GeoServer 的 WPS 之上，新增 **16 个自定义 WPS 处理过程**，覆盖三大类空间分析能力：

- **地形分析**：坡向、坡度；
- **适宜性 / 潜在分布建模**（物种栖息地建模方向）：最大熵（Maxent）、最大熵特征权重（MEF）、HSI 指数模型、PCA / 熵权法因子权重及对应的潜在分布预测；
- **栅格 / 矢量常规处理**：掩膜裁剪、范围裁剪、重分类、重投影、重采样、欧氏邻近分析、矢量属性筛选、栅格提取到点。

注册后，这些过程会出现在 GeoServer 的 `GetCapabilities` 中，可被任意 WPS 客户端通过标准 `Execute` 请求调用，实现**服务端按需地理空间分析**。

### 1.2 架构与实现方式

每个过程是一个独立的 Maven 子模块（`src/<Name>/`），严格遵循 GeoServer WPS 扩展开发规范：

- 实现 `GeoServerProcess` 接口（GeoTools / GeoServer process 框架）；
- 使用 `@DescribeProcess` / `@DescribeParameter` / `@DescribeResult` 注解声明过程标题、输入输出参数与数据类型；
- 通过 Spring 配置文件 `src/<Name>/src/main/resources/applicationContext.xml` 注册为 bean（`<bean id="<Name>" class="org.geoserver.<Name>"/>`），由 GeoServer 的 Spring 上下文自动扫描并暴露为 WPS 过程；
- 在聚合 POM `src/pom.xml` 中以 `<module>` 形式纳入构建，随 `mvn clean install` 一并编译安装进 GeoServer Web 应用。

> **计算实现分两类**：一类**在 JVM 内基于 GeoServer/GeoTools API 直接计算**（如 `CoverageClip`、`FilterAttributes`、`Reclassification` 等）；另一类（如 `Aspect`、`CalcSlope`、`EuclideanDistance`）通过 Java `ProcessBuilder` **桥接调用外部 Python / QGIS 脚本**（例如 `python-qgis.bat`）完成重计算，再将结果栅格读回返回，兼顾了复杂算法的复用与跨语言集成。

### 1.3 自定义 WPS 过程清单（16 个）

| 模块（目录） | WPS 过程名 | 功能说明 | 类别 |
|------|-----------|---------|------|
| `Aspect` | Aspect | 坡向分析：基于 DEM 计算地形坡向 | 地形分析 |
| `CalcSlope` | CalcSlope | 坡度分析：基于 DEM 计算坡度，支持 z 因子 | 地形分析 |
| `CoverageClip` | CoverageClip | 掩膜提取：按范围 / 边界对栅格裁剪掩膜 | 栅格处理 |
| `EntropyWeight` | EntropyWeight | 熵权法确定因子权重：多因子遥感影像熵权法综合评估 | 适宜性 / 权重 |
| `EuclideanDistance` | EuclideanDistance | 邻近分析：基于源像元计算欧氏距离（邻近度）栅格 | 栅格处理 |
| `FilterAttributes` | FilterAttributes | 矢量属性有效数据筛选：按有效属性占比阈值筛选要素 | 矢量处理 |
| `HSILatDis` | HSI Latent Distribution | HSI 指数模型预测潜在分布：加权多因子栖息地适宜性建模 | 适宜性建模 |
| `MEF` | MEF | 最大熵特征权重：活动点位 + 环境因子计算 MaxEnt 特征权重 | 分布建模 |
| `Maxent` | Maxent | 最大熵预测潜在分布：物种活动点位 + 环境因子预测潜在分布图 | 分布建模 |
| `PCA` | PCA | 主成分分析法确定因子权重：多因子 PCA 降维提取权重 | 适宜性 / 权重 |
| `PCALatDis` | PCA Latent Distribution | 主成分分析法预测潜在分布：基于 PCA 降维结果预测潜在分布 | 分布建模 |
| `Reclassification` | Reclassification | 重分类：按规则对栅格值重分类 | 栅格处理 |
| `PointExtraction` | pointExtraction | 栅格提取到点：将栅格值提取为点要素 | 矢量处理 |
| `RangeCoverageClip` | RangeCoverageClip | 按范围裁剪栅格：按数值范围裁剪栅格 | 栅格处理 |
| `Reprojection` | Reprojection | 重投影：对 coverage / 要素进行坐标系重投影 | 栅格 / 矢量 |
| `Resample` | Resample | 重采样：对栅格进行重采样 | 栅格处理 |

### 1.4 模块目录结构示例（`Aspect`）

```
src/Aspect/
├── pom.xml                                 # Maven 子模块
└── src/main/
    ├── java/org/geoserver/Aspect.java      # 实现 GeoServerProcess，含 @DescribeProcess 注解
    └── resources/applicationContext.xml    # Spring 注册：<bean id="Aspect" class="org.geoserver.Aspect"/>
```

### 1.5 调用方式

构建部署后，自定义过程出现在 WPS 能力文档中：

```
GET  /geoserver/ows?service=WPS&request=GetCapabilities
```

以标准 OGC WPS `Execute` 请求调用，例如执行 `Maxent` 潜在分布预测：

```
POST /geoserver/ows?service=WPS&request=Execute&version=1.0.0
```

请求体为 WPS Execute XML，指定过程标识 `Maxent` 及输入 coverage / 点位要素（`SimpleFeatureCollection`）。

---

## 二、构建与部署

沿用上游 GeoServer 构建方式；自定义 16 个模块已纳入聚合 POM（`src/pom.xml`），无需额外步骤：

```bash
cd src
mvn clean install
```

将生成物（`src/web/app` 目标 WAR，或通过 Jetty 启动）部署到 Servlet 容器即可。自定义 WPS 过程会随 GeoServer 启动由 Spring 上下文自动注册并对外暴露。

---

## 三、上游 GeoServer 说明（保留）

> 以下为 GeoServer 2.22.0 上游原始 README。本仓库**在此基础之上**新增了上述「自定义 WPS 处理过程代码层」，其余内容与原仓库一致。

<img src="/doc/en/themes/geoserver/static/GeoServer_500.png" width="353">

[![Gitter](https://badges.gitter.im/geoserver/geoserver.svg)](https://gitter.im/geoserver/geoserver?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![DOI](https://zenodo.org/badge/2751199.svg)](https://zenodo.org/badge/latestdoi/2751199)

[GeoServer](https://geoserver.org) is an open source software server written in Java that
allows users to share and edit geospatial data. Designed for interoperability, it publishes data from
any major spatial data source using open standards.

Being a community-driven project, GeoServer is developed, tested, and supported by a diverse group of
individuals and organizations from around the world.

GeoServer is the reference implementation of the Open Geospatial Consortium (OGC)
Web Feature Service (WFS) and Web Coverage Service (WCS) standards, as well as a high performance
certified compliant Web Map Service (WMS), compliant Catalog Service for the Web (CSW)
and implementing Web Processing Service (WPS).
GeoServer forms a core component of the Geospatial Web.

## License

GeoServer licensed under the [GPL](https://docs.geoserver.org/latest/en/user/introduction/license.html).

## Using

Please refer to the [user guide](https://docs.geoserver.org/latest/en/user/) for information
on how to install and use GeoServer.

## Building

GeoServer uses [Apache Maven](https://maven.apache.org/) for a build system. To
build the application run maven from the `src` directory.

    mvn clean install

See the [developer guide](https://docs.geoserver.org/latest/en/developer/)
for more details.

## Bugs

GeoServer uses [JIRA](https://osgeo-org.atlassian.net/projects/GEOS), hosted by
[Atlassian](https://www.atlassian.com/), for issue tracking.

## Mailing Lists

The [mailing list page](https://geoserver.org/comm/) on the GeoServer web site provides
access to the various mailing list, as well as some indication of the [code of conduct](https://geoserver.org/comm/userlist-guidelines.html) when posting to the lists

## Contributing

Please read [the contribution guidelines](https://github.com/geoserver/geoserver/blob/main/CONTRIBUTING.md) before contributing pull requests to the GeoServer project.

## More Information

Visit the [website](https://geoserver.org/) or read the [docs](https://docs.geoserver.org/).
