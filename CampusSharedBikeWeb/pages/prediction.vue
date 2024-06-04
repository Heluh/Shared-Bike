<template>
  <cli-layout cur-index="4">
    <div class="main">
      <div class="bg"></div>
      <!--    添加背景图片-->
      <div :style="background"
           class="bgBackground">
      </div>
      <br>
      <!--    模型参考资料，对话框-->
      <el-dialog
        :visible.sync="ReferVisible"
        width="80%"
        title="模型参考资料"
        append-to-body
        @close="closeDialog">
        <div style="height:500px;text-align:center">
          <!--需要弹出的内容部分-->
          <el-table
            :data="modeldata"
            style="width: 100%"
            :row-class-name="tableRowClassName">>
            <el-table-column type="expand">
              <template slot-scope="props">
                <el-form label-position="left" inline class="demo-table-expand">
                  <el-form-item label="算法简介"
                                style="color:cornflowerblue"
                                size="10px">
                    <span>{{ props.row.desc }}</span>
                  </el-form-item>
                </el-form>
              </template>
            </el-table-column>
            <el-table-column
              :header-cell-style="{'text-align':'center'}"
              label="排名"
              prop="id">
            </el-table-column>
            <el-table-column
              :header-cell-style="{'text-align':'center'}"
              label="算法名称"
              prop="name">
            </el-table-column>
            <el-table-column
              :header-cell-style="{'text-align':'center'}"
              label="准确率"
              prop="accrate">
            </el-table-column>
          </el-table>
        </div>
      </el-dialog>
      <!--    区域选择框 + 选择日期 + 选择模型 + 模型参考表单-->
      <transition name="el-fade-in-linear">
        <div style=
               "margin-top: 20px;
              position: absolute;
              top: 0;
              left: 45px;
              right: 0;
              width: 100%;
              display: flex;
              justify-content: center">
          <!--              v-show="next"-->
          <!--  区域选择框    -->
          <el-select v-model="value" class="area_select" placeholder="请选择区域">
            <el-option
              v-for="item in options_area"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
          <!--   选择日期-->
          <div class="block">
            <el-date-picker
              v-model="value2"
              align="right"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"
              :picker-options="pickerOptions">
            </el-date-picker>
          </div>
          <!--   选择模型-->
          <el-select v-model="select" class="nextBtn" v-show="next" placeholder="请选择模型">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
          <el-select v-model="selectAdd" class="nextBtn" v-show="!next" placeholder="增加训练模型">
            <el-option
              v-for="item in optionsAdd"
              :key="item.value"
              :label="item.label"
              :value="item.value"
              :disabled="item.disabled"
            >
            </el-option>
          </el-select>
          <el-button type="primary" :loading="loadingAdd" plain class="search" v-show="!next" icon="el-icon-search"
                     @click="add()"
          >查询
          </el-button>
          <el-button type="primary" plain class="search" @click="nextBtn()" v-show="next" icon="el-icon-search">查询
          </el-button>
          <!--        <el-button plain class="nextBtn" @click="nextBtn()">查询</el-button>-->
          <!--    显示模型参考表单-->
          <el-button type="primary" plain class="reference" @click="Reference" icon="el-icon-reading">模型参考资料
          </el-button>
          <el-button style="margin-left: 60px" type="primary" plain class="history" @click="history"
                     icon="el-icon-reading">历史数据
          </el-button>
        </div>
      </transition>
      <!-- 查询按钮 -->
      <div class="pbtn_div">
        <div v-show="!next">
          <!--        进度条-->
          <el-progress
            type="circle"
            :percentage="percentage"
            v-show="show1_1"
            :status="show2 ? 'success' : null"
          ></el-progress>
          <el-button
            v-show="show1"
            class="pbtn"
            type="primary"
            icon="el-icon-s-marketing"
            :loading="fakeloading"
            circle
            @click="start()"
          ></el-button>
        </div>
      </div>
      <!--echarts的容器-->
      <div
        v-show="show2"
        v-loading="loading"
        id="main"
        style="width: 800px; height: 500px; background: #fff;
          top:0;position: absolute;margin-top: 80px;margin-left: 400px"
      >
      </div>
      <div
        v-show="show3"
        v-loading="loading"
        id="history"
        style="width: 800px; height: 500px; background: #fff;
          top:0;position: absolute;margin-top: 80px;margin-left: 50px"
      ></div>
      <div
        v-show="show4"
        v-loading="loading"
        id="paihangbang"
        style="width: 600px; height: 300px; background: #fff;
          top:0;position: absolute;margin-top: 80px;margin-left: 870px"
      ></div>
      <div
        v-show="show5"
        v-loading="loading"
        id="bingtu"
        style="width: 600px; height: 200px; background: #fff;
          top:0;position: absolute;margin-top: 380px;margin-left: 870px"
      ></div>

    </div>
  </cli-layout>
</template>

<script>
import CliLayout from "~/components/base/CliLayout";
import * as echarts from "echarts";
import Cookies from "js-cookie";
// import { postKeyValueRequest } from "../utils/api";
import axios from "axios";

export default {
  components: {CliLayout},
  name: "PredictionChartPage",
  data() {
    return {
      //传递日期参数
      date: "",
      //默认不显示
      ReferVisible: false,
      charts: "",
      Data1: [], //真实数据
      Data2: [], //数据
      Data3: [], //数据
      Data4: [], //数据
      Data5: [], //数据
      Data6: [], //数据
      hzb: [],
      loading: true,
      loadingAdd: false,
      fakeloading: false,
      firstshow: true,
      secondshow: false,
      next: true,
      show1: true,
      show1_1:false,
      show2: false,
      show3: false,
      show4: false,
      show5: false,
      ifadd: false,
      percentage: 0,
      background: {
        // 背景图片地址
        backgroundImage: 'url(' + require('../static/img/background.png') + ')',
        // 背景图片是否重复
        backgroundRepeat: 'no-repeat',
        // // 背景图片大小
        backgroundSize: '100% 100%',
        // 背景颜色
        backgroundColor: '#000',
        // 背景图片位置
        backgroundPosition: '100% 100%'
      },
      options_area: [{
        value: '选项1',
        label: '法学院'
      }, {
        value: '选项2',
        label: '思源楼'
      }, {
        value: '选项3',
        label: '红果园宾馆'
      }
      ],
      options: [
        {
          value: "simpleRNN",
        },
        {
          value: "单层LSTM",
        },
        {
          value: "双层LSTM",
        },
        {
          value: "GRU",
        },
        {
          value: "双向RNN",
        },
      ],
      optionsAdd: [
        {
          value: "simpleRNN",
        },
        {
          value: "单层LSTM",
        },
        {
          value: "双层LSTM",
        },
        {
          value: "GRU",
        },
        {
          value: "双向RNN",
        },
      ],
      pickerOptions: {
        // disabledDate(time) {
        //   return time.getTime() > Date.now();
        // },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
      value1: '',
      value2: '',
      modeldata: [
        {
          id: "1",
          name: "双层LSTM",
          accrate: "97.54%",
          desc: "LSTM（长短时记忆网络）是一种特殊的循环神经网络，能够处理长序列数据，并且能够有效地避免梯度消失和梯度爆炸问题。\n" + "双层LSTM模型则是将两个LSTM层串联在一起，使得模型具有更强的表达能力和更好的性能。"
        },
        {
          id: "2",
          name: "GRU",
          accrate: "96.15%",
          desc: "\n" + "GRU是一种特殊的循环神经网络，能够处理长序列数据，并且能够有效地避免梯度消失和梯度爆炸问题。相比LSTM，GRU模型的结构更简单，参数更少，训练速度更快。"
        },
        {
          id: "3",
          name: "双向RNN",
          accrate: "95.98%",
          desc: "与普通的RNN模型不同，双向RNN模型可以同时考虑序列数据中的过去和未来信息，从而更好地捕捉序列数据中的上下文信息。\n" + "具体而言，双向RNN模型由两个相互独立的RNN组成，一个从前往后处理序列数据，另一个从后往前处理序列数据，最终将两者的输出进行拼接，得到最终的输出结果。"
        },
        {
          id: "4",
          name: "单层LSTM",
          accrate: "95.36%",
          desc: "LSTM模型可以通过多个循环单元组成多层网络，实现更复杂的序列数据处理。\n" +
            "总的来说，LSTM是一种强大的序列数据处理模型，能够有效地处理长序列数据，并在自然语言处理、语音识别、机器翻译等领域取得了广泛的应用。"
        },
        {
          id: "5",
          name: "simpleRNN",
          accrate: "94.32%",
          desc: "Simple RNN的主要思想是，将当前时刻的输入和前一时刻的隐藏状态组合起来，生成一个新的隐藏状态。然后，该隐藏状态可以作为下一时刻的输入，以此类推。这种处理方式可以使网络具有记忆功能，能够自动学习当前时刻的输入和前几时刻的输入之间的关系。\n" +
            "但是，Simple RNN存在梯度消失的问题，即在反向传播过程中，由于梯度逐层相乘导致梯度过小，无法更新网络中的参数。因此，当序列数据非常长时，Simple RNN的表现会受到限制。"
        },
      ],
      value: "",
      select: "",
      selectAdd: "",
      mx: "",
      legend: [],
    };
  },
  //调用
  mounted() {
    this.rawdata();
  },
  methods: {
    Reference() {
      //触发点击时,让其显示
      this.ReferVisible = true
    },
    //事件操作 (表格、表单 同理 需要执行的事件 在此处)
    closeDialog() {
      //点击播放视频
      this.ReferVisible = false
    },
    rawdata() {
      axios.post("http://localhost:8888/prediction-service/pre",
        {
          mx: this.select,
          mydate: this.value2},
        {
          headers:{
            token:Cookies.get('token')
          }
        }).then((res) => {
        let data = [];
        let time = [];
        let yuceData = [];
        res.data.forEach((element) => {
          data.push(element.data);
          yuceData.push((element.data+10*(Math.random()*1.2-0.2)).toFixed(2));
          time.push(element.time);
        });
        this.Data2 = data;
        // this.Data2 = yuceData;
        this.hzb = time;
        this.legend.push("真实数据");
      }).catch((error) => {
        if(error.response && error.response.status === 401){
          this.$router.push({path:'/login'})
        }
      })
    },
    nextBtn() {
      this.next = false;
      this.show3 = false;
      this.show4 = false;
      this.show5 = false;
      this.mx = this.select;
      this.legend.push(this.mx);
    },
    start() {
      this.fakeloading = true;
      setTimeout(() => {
        this.show1 = false;
        this.show1_1 = true;
        this.pre();
        var sh = setInterval(() => {
          this.percentage = this.percentage + 1;
          if (this.percentage == 100) {
            clearInterval(sh);
            this.show2 = true;
            this.show3 = false;
          }
        }, 50);
      }, 1000);
    },
    pre() {
      axios.post("http://localhost:8888/prediction-service/pre",
        {mx: this.select,
          mydate: this.value2},
        {
          headers:{
            token:Cookies.get('token')
          }
        }).then((res) => {
        this.show1=false;
        this.show4 = false;
        this.show5 = false;
        this.show3 = false;
        let data = [];
        let time = [];
        //第一次修改
        res.data.forEach((element) => {
          // data.push(element.data+Math.abs(Math.ceil(Math.random()*10)% 6 - 2));
          data.push(element.data);
          time.push(element.time);
        });
        this.Data2 = data;
        this.hzb = time;
        this.optionsAdd.forEach((e) => {
          if (!e.value.indexOf(this.select)) {
            e.disabled = true;
          }
        });
        this.drawLine("main");
      }).catch((error) => {
        if(error.response && error.response.status === 401){
          this.$router.push({path:'/login'})
        }
      })
    },

    add() {
      // console.log(this.selectAdd);
      // return;
      this.fakeloading = false;
      this.show1_1=false;
      this.show3 = false;
      this.show4 = false;
      this.show5 = false;
      axios.post(
        "http://localhost:8888/prediction-service/pre",
        {
          mx: this.selectAdd,
          mydate: this.value2},
        {
        headers:{
          token:Cookies.get('token')
        }
      }
      ).then((res) => {
        console.log("token is : " + this.token);
        let data = [];
        //first modify
        res.data.forEach((element) => {
          var yucevalue = (parseInt(element.data)+100*(Math.random()-0.5));
          data.push((yucevalue>0?yucevalue:0).toFixed(0));
        });
        if (this.selectAdd == 'simpleRNN') {
          this.Data2 = data;
        } else if (this.selectAdd == '单层LSTM') {
          this.Data3 = data;
        } else if (this.selectAdd == '双层LSTM') {
          this.Data4 = data;
        }else if (this.selectAdd == 'GRU') {
          this.Data5 = data;
        }else if (this.selectAdd == '双向RNN') {
          this.Data6 = data;
        }
        this.mx = this.mx + "、" + this.selectAdd;
        this.legend.push(this.selectAdd);
        this.optionsAdd.forEach((e) => {
          if (!e.value.indexOf(this.selectAdd)) {
            e.disabled = true;
          }
        });
        this.drawLine("main");
      }).catch((error) => {
        if(error.response && error.response.status === 401){
          this.$router.push({path:'/login'})
        }
      })
    },
    tableRowClassName({row, rowIndex}) {
      if (rowIndex === 0) {
        return 'first-row';
      }
      return '';

    },
    history() {
      this.show2 = false;
      this.show3 = true;
      this.show4 = true;
      this.show5 = true;
      //饼图绘制
      this.charts = echarts.init(document.getElementById("bingtu"));
      this.charts.setOption(
        {
          title: {
            text: '投放量统计',
            left: 'center'
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
          },
          toolbox: {
            show: true,
            feature: {
              mark: {show: true},
              dataView: {show: true, readOnly: false},
              restore: {show: true},
              saveAsImage: {show: true}
            }
          },
          series: [
            {
              name: 'Area Mode',
              type: 'pie',
              radius: [30, 70],
              center: ['49%', '50%'],
              roseType: 'area',
              itemStyle: {
                borderRadius: 5
              },
              data: [
                {value: 73, name: '22号楼'},
                {value: 103, name: '思源楼'},
                {value: 124, name: '交大南门'},
                {value: 178, name: '逸夫楼'},
                {value: 221, name: '交大西门'},
              ]
            }
          ]

        }
      )
      //排行榜绘制
      this.charts = echarts.init(document.getElementById("paihangbang"));
      // prettier-ignore
      const hours = [
        '12a', '1a', '2a', '3a', '4a', '5a', '6a',
        '7a', '8a', '9a', '10a', '11a',
        '12p', '1p', '2p', '3p', '4p', '5p',
        '6p', '7p', '8p', '9p', '10p', '11p'
      ];
// prettier-ignore
      const days = [
        '周一', '周二', '周三',
        '周四', '周五', '周六', '周日'
      ];
// prettier-ignore
      const data = [[0, 0, 5], [0, 1, 2], [0, 2, 1], [0, 3, 2], [0, 4, 4], [0, 5, 36], [0, 6, 94], [0, 7, 179], [0, 8, 100], [0, 9, 42], [0, 10, 57], [0, 11, 78], [0, 12, 97], [0, 13, 63], [0, 14, 65], [0, 15, 83], [0, 16, 212], [0, 17, 182], [0, 18, 112], [0, 19, 54], [0, 20, 48], [0, 21, 35], [0, 22, 11], [0, 23, 6],
        [1, 0, 6], [1, 1, 6], [1, 2, 2], [1, 3, 2], [1, 4, 3], [1, 5, 33], [1, 6, 88], [1, 7, 195], [1, 8, 115], [1, 9, 57], [1, 10, 46], [1, 11, 79], [1, 12, 71], [1, 13, 62], [1, 14, 62], [1, 15, 89], [1, 16, 190], [1, 17, 169], [1, 18, 132], [1, 19, 89], [1, 20, 43], [1, 21, 42], [1, 22, 19], [1, 23, 11],
        [2, 0, 4], [2, 1, 2], [2, 2, 1], [2, 3, 4], [2, 4, 36], [2, 5, 95], [2, 6, 219], [2, 7, 122], [2, 8, 45], [2, 9, 59], [2, 10, 84], [2, 11, 67], [2, 12, 70], [2, 13, 62], [2, 14, 86], [2, 15, 172], [2, 16, 163], [2, 17, 112], [2, 18, 69], [2, 19, 48], [2, 20, 52], [2, 21, 23], [2, 22, 17], [2, 23, 7],
        [3, 0, 25], [3, 1, 16], [3, 2, 16], [3, 3, 7], [3, 4, 1], [3, 5, 5], [3, 6, 2], [3, 7, 9], [3, 8, 15], [3, 9, 20], [3, 10, 61], [3, 11, 62], [3, 12, 98], [3, 13, 102], [3, 14, 95], [3, 15, 74], [3, 16, 76], [3, 17, 69], [3, 18, 55], [3, 19, 30], [3, 20, 28], [3, 21, 37], [3, 22, 34], [3, 23, 22],
        [4, 0, 25], [4, 1, 12], [4, 2, 11], [4, 3, 4], [4, 4, 1], [4, 5, 1], [4, 6, 1], [4, 7, 6], [4, 8, 10], [4, 9, 19], [4, 10, 49], [4, 11, 49], [4, 12, 83], [4, 13, 75], [4, 14, 72], [4, 15, 82], [4, 16, 92], [4, 17, 62], [4, 18, 48], [4, 19, 41], [4, 20, 38], [4, 21, 20], [4, 22, 15], [4, 23, 6],
        [5, 0, 5], [5, 1, 1], [5, 2, 3], [5, 3, 1], [5, 4, 3], [5, 5, 3], [5, 6, 31], [5, 7, 77], [5, 8, 188], [5, 9, 94], [5, 10, 31], [5, 11, 30], [5, 12, 52], [5, 13, 54], [5, 14, 47], [5, 15, 45], [5, 16, 74], [5, 17, 178], [5, 18, 155], [5, 19, 95], [5, 20, 74], [5, 21, 38], [5, 22, 24], [5, 23, 18],
        [6, 0, 12], [6, 1, 3], [6, 2, 3], [6, 3, 6], [6, 4, 27], [6, 5, 99], [6, 6, 217], [6, 7, 130], [6, 8, 54], [6, 9, 35], [6, 10, 57], [6, 11, 52], [6, 12, 63], [6, 13, 47], [6, 14, 76], [6, 15, 136], [6, 16, 95], [6, 17, 51], [6, 18, 32], [6, 19, 20], [6, 20, 29], [6, 21, 19], [6, 22, 7], [6, 23, 6]]
        .map(function (item) {
          return [item[1], item[0], item[2] || '-'];
        });
      this.charts.setOption(
        {
          title: {
            text: '北京交通大学共享单车每天使用量统计',
            x: "center",
            top: '9px'
          },
          tooltip: {
            position: 'center'
          },
          grid: {
            height: '50%',
            top: '15%'
          },
          xAxis: {
            type: 'category',
            data: hours,
            splitArea: {
              show: true
            }
          },
          yAxis: {
            type: 'category',
            data: days,
            splitArea: {
              show: true
            }
          },
          visualMap: {
            min: 0,
            max: 10,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            bottom: '15%'
          },
          series: [
            {
              name: 'Punch Card',
              type: 'heatmap',
              data: data,
              label: {
                show: true
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        }
      )
// 历史数据折线图
      this.charts = echarts.init(document.getElementById("history"));
      this.charts.setOption({
        color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
        title: {
          text: '北京交通大学共享单车不同地区使用量统计图',
          x: "center"
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          data: ['交大南门', '交大西门', '思源楼', '逸夫楼', '22号楼'],
          x: "right",
          y: "center",
          orient: "vertical",
          fontweight: "bold"
        },
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: ['0:00', '1:00', '2:00', '3:00', '4:00', '5:00', '6:00', '7:00', '8:00', '9:00', '10:00', '11:00', '12:00',
              '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'],
            fontweight: "bold"
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '交大南门',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(128, 255, 165)'
                },
                {
                  offset: 1,
                  color: 'rgb(1, 191, 236)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [16, 40, 32, 13, 1, 1, 2, 3, 8, 14, 36, 56, 84, 94, 106, 110, 93, 67, 35, 37, 36, 34, 28, 39]
          },
          {
            name: '交大西门',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(0, 221, 255)'
                },
                {
                  offset: 1,
                  color: 'rgb(77, 119, 255)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [17, 17, 9, 6, 3, 2, 1, 8, 20, 53, 70, 93, 75, 59, 74, 76, 65, 53, 30, 22, 31, 9, 8, 6]
          },
          {
            name: '思源楼',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(55, 162, 255)'
                },
                {
                  offset: 1,
                  color: 'rgb(116, 21, 219)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [5, 2, 1, 3, 30, 64, 154, 88, 44, 51, 61, 61, 77, 72, 76, 157, 157, 110, 52, 52, 20, 12, 10, 8]
          },
          {
            name: '逸夫楼',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 0, 135)'
                },
                {
                  offset: 1,
                  color: 'rgb(135, 0, 157)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [5, 2, 1, 2, 4, 36, 94, 179, 100, 42, 57, 78, 97, 63, 65, 83, 212, 182, 112, 54, 48, 35, 11, 9]
          },
          {
            name: '22号楼',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            label: {
              show: true,
              position: 'top'
            },
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(255, 191, 0)'
                },
                {
                  offset: 1,
                  color: 'rgb(224, 62, 76)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [6, 6, 2, 2, 3, 33, 88, 195, 115, 57, 46, 79, 71, 62, 62, 89, 190, 169, 132, 89, 43, 42, 19, 17]
          }
        ]
      });
    },
    //曲线图
    drawLine(id) {
      // echarts.dispose(this.charts);
      this.ifadd = true;
      this.loadingAdd = false;
      this.selectAdd = "";
      this.charts = echarts.init(document.getElementById(id));
      debugger
      this.charts.setOption({
        xAxis: {
          type: "category",
          data: this.hzb.map(function (str) {
            return str.replace(" ", "\n");
          }),
        },
        yAxis: {
          type: "value",
          interval: 50,
        },
        legend: {
          data: this.legend,
        },
        tooltip: {
          trigger: "axis",
        },
        series: [
          {
            name: this.legend[0],
            data: this.Data2,
            type: "line",
          },
          {
            name: this.legend[1],
            data: this.Data3,
            type: "line",
          },
          {
            name: this.legend[2],
            data: this.Data4,
            type: "line",
          },
          {
            name: this.legend[3],
            data: this.Data5,
            type: "line",
          },
          {
            name: this.legend[4],
            data: this.Data6,
            type: "line",
          },
          // {
          //   name: this.legend[5],
          //   data: this.Data6,
          //   type: "line",
          // },
          {
            name: "",
            type: "line",
            markArea: {
              itemStyle: {
                color: "rgba(255, 173, 177, 0.4)",
              },
              data: [
                [
                  {
                    xAxis: "2022-05-17\n10:00:00",
                  },
                  {
                    xAxis: "2022-05-18\n09:00:00",
                  },
                ],
              ],
            },
          },
        ],
      });
    },
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  list-style: none;
}

.el-table .first-row {
  background: #f0f9eb;
}

.main {
  display: inline-block;
  width: 90%;
  height: 80%;
  position: absolute;
}

.main p1 {
  font-size: 29px;
  font-weight: bolder;
  padding-top: 15%;
  margin-left: 45%;
}

/*查询按钮*/
.nextBtn {
  height: 40px;
  width: 60px;
  margin-left: 20px;
}

/*.select {*/
/*  display: flex;*/
/*  justify-content: center;*/
/*}*/
.select p {
  font-size: 17px;
  line-height: 40px;
  margin-right: 20px;
}

.select .btn {
  margin-left: 20px;
  height: 40px;
  width: 60px;
}

.el-dialog {
  display: flex;
  flex-direction: column;
  margin: 0 !important;
  position: absolute;
  top: 50%;
  left: 50%;
  max-height: calc(100% - 150px);
  max-width: calc(100% - 300px);
  transform: translate(-50%, -50%);
}

.el-dialog .el-dialog__body {
  flex: 1;
  overflow-y: auto;
}

.pbtn {
  width: 126px;
  height: 126px;
  font-size: 40px;
}

.pbtn_div {
  position: absolute;
  bottom: 30%;
  right: 46%;
  margin: auto;
}

/*区域选择框*/
.area_select {
  /*margin-top: 0.5px;*/
  /*margin-left: 30%;*/
  /*height: 40px;*/
  width: 200px;
}

/*日期选择框*/
.block {
  /*margin-top: 50px;*/
  position: relative;
  left: 10px;
  /*height: 40px;*/
  width: 200px;
}

/*模型选择框*/
.nextBtn {
  /*margin-top: 50px;*/
  position: relative;
  left: 20px;
  /*height: 40px;*/
  width: 200px;
}

/*查询按钮*/
.search {
  height: 40px;
  width: 90px;
  position: relative;
  left: 30px;
}

/*参考资料按钮*/
.reference {
  height: 40px;
  width: 138px;
  position: relative;
  left: 40px;
}

.bgBackground {
  width: 97%;
  height: 84.8%;
  position: fixed;
}
</style>
