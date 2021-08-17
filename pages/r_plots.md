---
title: FSK-Lab
summary: 
keywords: TODO
sidebar: fsk_sidebar
permalink: r_plots.html
folder: home
---

# Introduction
FSK-Lab does not support every R package for plotting or creating graphics. This page lists the supported pages and working examples.

<div class="row">
  <div class="col-lg-12">
    <h2 class="page-header">Supported R plots</h2>
  </div>

  <div class="col-md-3 col-sm-6">
    <div class="panel panel-default text-center">
      <div class="panel-heading">
        <img src="assets/R_logo64.png">
      </div>
      <div class="panel-body">
        <h4>R base</h4>
        <p>Graphics generated with R Base</p>
        <a href="#r-base-plots" class="btn btn-primary">Learn More</a>
      </div>
    </div>
  </div>

  <div class="col-md-3 col-sm-6">
    <div class="panel panel-default text-center">
      <div class="panel-heading">
        <img src="assets/ggplot2.png">
      </div>
      <div class="panel-body">
        <h4>ggplot2</h4>
        <p>Declarative creation of graphics</p>
        <a href="#ggplot2-plots" class="btn btn-primary">Learn More</a>
      </div>
    </div>
  </div>
</div>

# R Base plots
Graphics generated with R Base. They include histograms, line graphs, scatterplots and boxplots. See more at [R graphics package].

Example:

```R
hist(airquality$Temp)
```

![](assets/plot/example_rbase.png)

# ggplot2 plots
ggplot2 is a system for declaratively creating graphics, based on The Grammar of Graphics. You provide the data, tell ggplot2 how to map variables to aesthetics, what graphical primitives to use, and it takes care of the details. See more at the [ggplot2 site].

Example:

```R
library(ggplot2)
ggplot(mtcars, aes(x=as.factor(cyl), fill=as.factor(cyl) )) +
  geom_bar( ) +
  scale_fill_hue(h = c(180, 300))
```

![](assets/plot/example_ggplot2.png)


[R graphics package]: https://stat.ethz.ch/R-manual/R-devel/library/graphics/html/00Index.html
[ggplot2 site]: https://ggplot2.tidyverse.org
