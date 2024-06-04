<template>
  <div class="hx">
    <div :id="chartId"/>
  </div>
</template>

<script>
export default {
  name: "AreaGraph",
  props: {
    chartId: {
      type: String,
      default: "main",
    },
  },
  data() {
    return {
      chart: null,
      titleText: "",
      titleSubtext: "",
      legendData: [],
      seriesName: "",
      seriesData: [],
    };
  },
  methods: {
    setGraphData(data) {
      this.titleText = data.titleText;
      this.titleSubtext = data.titleSubtext;
      this.legendData = data.legendData;
      this.seriesName = data.seriesName;
      this.seriesData = data.seriesData;

      this.initGraph();
    },

    initGraph() {
      let chartDom = document.getElementById(this.chartId);
      this.chart = this.$echarts.init(chartDom);

      let option = {
        title: {
          text: this.titleText,
          subtext: this.titleSubtext,
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          top: 'bottom'
        },
        toolbox: {
          show: true,
          feature: {
            mark: {show: true},
            dataView: {show: false},
            restore: {show: false},
            saveAsImage: {show: true}
          }
        },
        series: [
          {
            name: this.seriesName,
            type: 'pie',
            radius: [20, 140],
            center: ['25%', '50%'],
            roseType: 'radius',
            itemStyle: {
              borderRadius: 5
            },
            emphasis: {
              label: {
                show: true
              }
            },
            data: this.seriesData
          }
        ]
      };

      this.chart.setOption(option);
    },

  },
}
</script>

<style scoped>
.hx {
  width: 100%;
  height: 100%;
}
</style>
