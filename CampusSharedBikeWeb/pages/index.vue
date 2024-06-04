<template>
  <cli-layout>
    <!--这里是系统首页-->
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-people">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-ren"></use>
            </svg>
          </div>
          <div class="card-panel-description"
               style="display: flex;justify-content: center;align-items: center;margin-top: 40px">
            <div class="card-panel-text">
              尊敬的管理员,
            </div>
            <div class="card-panel-text">
              您好！
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-message">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-tianqi"></use>
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text"
                 style="display: flex;justify-content: center;align-items: center;margin-top: -5px">
              北京市海淀区
            </div>
            <div style="display: flex;justify-content: center;align-items: center;">
              <svg class="icon" aria-hidden="true">
                <use xlink:href="#icon-tianqi-qing"></use>
              </svg>
              <div style="margin-left: 10px">
                晴 空气良 37/18℃
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('income')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-shouru"></use>
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text"
                 style="display: flex;justify-content: center;align-items: center;margin-top: -5px">
              今日收入
            </div>
            <div style="display: flex;justify-content: center;align-items: center;">
              <count-to :start-val="0" :end-val="32280" :duration="3200" class="card-panel-num"/>
              <div style="margin-left: 10px">
                ￥
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('bikeNums')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <svg class="icon" aria-hidden="true">
              <use xlink:href="#icon-zihangche"></use>
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text"
                 style="display: flex;justify-content: center;align-items: center;margin-top: -5px">
              当前投放量
            </div>
            <div style="display: flex;justify-content: center;align-items: center;">
              <count-to :start-val="0" :end-val="10280" :duration="3200" class="card-panel-num"/>
              <div style="margin-left: 10px">
                辆
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData"/>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card>
          <div class="chart-wrapper">
            <raddar-chart/>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card>
          <div class="chart-wrapper">
            <pie-chart/>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card>
          <div class="chart-wrapper">
            <bar-chart/>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </cli-layout>
</template>

<script>
import CliLayout from "~/components/base/CliLayout";
import LineChart from "~/components/dashboard/LineChart";
import CountTo from 'vue-count-to';
import Cookies from "js-cookie";
import "~/static/css/iconfont.js";
import RaddarChart from "~/components/dashboard/RaddarChart.vue";
import PieChart from "~/components/dashboard/PieChart.vue";
import BarChart from "~/components/dashboard/BarChart.vue";

const lineChartData = {
  income: {
    expectedData: [52049, 62049, 55279, 57482, 65741, 42039, 37685],
    actualData: [42049, 57049, 63279, 58482, 54009, 34039, 44685]
  },
  bikeNums: {
    expectedData: [12049, 9249, 15279, 14482, 13741, 11039, 9865],
    actualData: [10049, 11249, 14279, 16482, 12741, 11000, 10865]
  }
}

export default {
  name: 'IndexPage',
  components: {
    BarChart,
    PieChart,
    RaddarChart,
    CliLayout,
    LineChart,
    CountTo
  },
  data() {
    return {
      currentRole: 'adminDashboard',
      lineChartData: lineChartData.income
    }
  },
  mounted() {
    this.autoLogin();
  },
  methods: {
    autoLogin() {
      let token = Cookies.get('token');
      if (token) {
        this.$router.push({path: '/'});
      } else {
        this.$router.push({path: '/login'});
      }
    },
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style scoped>
.icon {
  width: 2em;
  height: 2em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 20px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
    //float: right; font-weight: bold;
      margin: 26px 26px 26px 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;
  }

  .chart-wrapper {
    padding: 8px;
  }
}
</style>
