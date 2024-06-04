/**
* @Author 王姝昕
* @Date 2023年05月21日 21:38
* @Description 车辆信息管理页面
* 
*/
<template>
  <cli-layout cur-index="5-1">
    <div >
      <el-card class="card">
      <!--导航栏设计-->
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item>
          <el-input v-model="search" class="search_name" size="mini" placeholder="输入车辆的编号进行查询"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="text" @click="onSearch()" class="el-icon-search">查询</el-button>
        </el-form-item>

        <el-form-item>
          <el-button class="el-icon-refresh" type="text" @click="refreshData">刷新</el-button>
        </el-form-item>

        <el-form-item>
          <el-button class="el-icon-circle-plus-outline" type="text" @click="emptyBikeData();dialogVisible = true">添加</el-button>
        </el-form-item>

      </el-form>

      <el-table :data="tableData" highlight-current-row border style="width: 100%">

        <el-table-column label="序号">
          <template slot-scope="scope">
            <span>{{ scope.$index+1 }}</span>
          </template>
        </el-table-column>

        <el-table-column label="车辆编号">
          <template slot-scope="scope">
            <span>{{ scope.row.bikeNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status }}</span>
          </template>
        </el-table-column>

        <el-table-column label="最近一次更新时间">
          <template slot-scope="scope">
            <span>{{ scope.row.lastUpdateTime }}</span>
          </template>
        </el-table-column>

        <el-table-column label="创建时间">
          <template slot-scope="scope">
            <span>{{ scope.row.createTime }}</span>
          </template>
        </el-table-column>

        <el-table-column label="围栏编号">
          <template slot-scope="scope">
            <span>{{ scope.row.fenceId }}</span>
          </template>
        </el-table-column>

        <el-table-column label="经度">
          <template slot-scope="scope">
            <span>{{ scope.row.longitude }}</span>
          </template>
        </el-table-column>

        <el-table-column label="纬度">
          <template slot-scope="scope">
            <span>{{ scope.row.latitude }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="190">
          <template slot-scope="scope">
            <el-button size="mini" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">修改</el-button>
            <el-button size="mini" icon="el-icon-delete" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--添加对话框开始-->
      <el-form :model="ruleForm" :ref="ruleForm" label-width="70px" class="demo-ruleForm" size="medium">
        <el-dialog title="添加" :append-to-body='true' :visible.sync="dialogVisible" width="50%" :before-close="handleClose">
          <el-input type="hidden" v-model="ruleForm.id"/>

      
          <el-form-item label="车辆编号" prop="bikeNumber">
            <el-input v-model="ruleForm.bikeNumber"></el-input>
          </el-form-item>
        
          <el-form-item label="状态" prop="status">
            <el-select v-model="ruleForm.status" placeholder="请选择使用状态">
              <el-option label="未使用" value="未使用"></el-option>
              <el-option label="使用中" value="使用中"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="经度" prop="longitude">
            <el-input v-model="ruleForm.longitude"></el-input>
          </el-form-item>
          <el-form-item label="纬度" prop="latitude">
            <el-input v-model="ruleForm.latitude"></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker v-model="ruleForm.createTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="最近一次更新时间" prop="lastUpdateTime">
            <el-date-picker v-model="ruleForm.lastUpdateTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="围栏编号" prop="fenceId">
            <el-input v-model="ruleForm.fenceId"></el-input>
          </el-form-item>

            <span slot="footer" class="dialog-footer">
              <el-button @click="cancel()" size="medium">取 消</el-button>
              <el-button @click="addBike()" type="primary" size="medium">确 定</el-button>
            </span>
        </el-dialog>
      </el-form>
      <!--添加对话框结束-->

      <!--修改对话框开始-->
      <el-form :model="ruleForm" :ref="ruleForm" label-width="70px" class="demo-ruleForm" size="medium">
        <el-dialog title="修改" :append-to-body='true' :visible.sync="dialogUpdate" width="50%" :before-close="handleClose">
          <el-input type="hidden" v-model="ruleForm.id"/>

          <el-form-item label="车辆编号" prop="bikeNumber" >
            <el-input v-model="ruleForm.bikeNumber" :readonly="true"></el-input>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="ruleForm.status" placeholder="请选择使用状态">
              <el-option label="未使用" value="未使用"></el-option>
              <el-option label="使用中" value="使用中"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="经度" prop="longitude">
            <el-input v-model="ruleForm.longitude"></el-input>
          </el-form-item>
          <el-form-item label="纬度" prop="latitude">
            <el-input v-model="ruleForm.latitude"></el-input>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker v-model="ruleForm.createTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="最近一次更新时间" prop="lastUpdateTime">
            <el-date-picker v-model="ruleForm.lastUpdateTime" type="datetime" placeholder="选择日期时间" value-format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="围栏编号" prop="fenceId">
            <el-input v-model="ruleForm.fenceId"></el-input>
          </el-form-item>

          <span slot="footer" class="dialog-footer">
              <el-button @click="cancel()" size="medium">取 消</el-button>
              <el-button @click="updateBike()" type="primary" size="medium">确 定</el-button>
            </span>
        </el-dialog>
      </el-form>
      <!--修改对话框结束-->

      <br>
      <div class="pages">
        <el-pagination background :disabled = "disablePage" :current-page.sync="currentPage" small layout="prev, pager, next" :page-size="pageSize" :total="total" @current-change="handleCurrentChange"></el-pagination>
      </div>
    </el-card>
    </div>
  </cli-layout>
</template>
  
<script>
import CliLayout from "~/components/base/CliLayout";
import axios from 'axios';
import qs from 'qs';
import Cookies from "js-cookie";
import 'default-passive-events';
  export default {
    name: "bike_manage",
    components: {CliLayout},
    data () {
    return {
      userData: '',
      ruleForm: {
        id: '',
        bikeNumber: '',
        status: '',
        longitude: '',
        latitude: '',
        createTime: '',
        lastUpdateTime: '',
        fenceId: ''
      },
      tableData: [],
      data:[],
      search: '',
      dialogVisible: false,
      dialogUpdate: false,
      pageSize: 5,
      currentPage: 1,
      total: 0,
      disablePage: false
    }
  },
  // 获取所有数据，刷新页面
  created () {
    const _this = this
    axios({
      url: 'http://localhost:8888/Bike-service/bike/showBike',
      method: 'get',
      headers: {
          token: Cookies.get('token')
        },
    }).then((response) => {
      if (response.data === 'fail') {
        console.log('校验失败')
      } else {
        _this.tableData = response.data.bikeList
        console.log(response.data.bikeList)
      }
    })
    
  },
    methods: {
    handleDelete (index, row) { // -----------------------------------------删除操作
      console.log('index======' + index)
      console.log('id=======' + row.id)
      this.$confirm('删除操作, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => {
            // 点击确认按钮后的操作
            const postData = qs.stringify({
              id: row.id
            })
            axios({
              method: 'post',
              url: 'http://localhost:8888/Bike-service/bike/deleteBike',
              data: postData,
              headers: {
                token: Cookies.get('token')
              },
            }).then(response=>{
              this.getBike()
            })
            this.$message({
                type: 'success',
                message: '删除成功!'
            });
            }).catch(() => {
            //点击取消按钮或关闭对话框后的操作
            this.$message({
                type: 'info',
                message: '已取消删除'
            });
        });
    },
    handleClose (done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
        })
        .catch(_ => {})
    },
    handleCurrentChange () {
      console.log(`当前页: ${this.currentPage}`)
      const postData = this.qs.stringify({
        page: this.currentPage
      })
      this.axios({
        method: 'post',
        url: 'http://localhost:8888/Bike-service/bike/showBike',
        data: postData,
        headers: {
          token: Cookies.get('token')
        },
      }).then(response => {
        this.tableData = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    cancel () {
      this.dialogUpdate = false
      this.dialogVisible = false
      this.emptyBikeData()
    },
    /* 表格清空函数 */
    emptyBikeData () {
      this.ruleForm = {
        id: '',
        bikeNumber: '',
        status: '',
        longitude: '',
        latitude: '',
        createTime: '',
        lastUpdateTime: '',
        fenceId: ''
      }
    },
    addBike () { // ----------------------------------------------添加操作
      const postData = qs.stringify({
        // id: this.ruleForm.id,
        bikeNumber: this.ruleForm.bikeNumber,
        status: this.ruleForm.status,
        longitude: this.ruleForm.longitude,
        latitude: this.ruleForm.latitude,
        createTime: this.ruleForm.createTime,
        lastUpdateTime: this.ruleForm.lastUpdateTime,
        fenceId: this.ruleForm.fenceId
      })
 
      axios({
        method: 'post',
        url: 'http://localhost:8888/Bike-service/bike/addBike',
        data: postData,
        headers: {
          token: Cookies.get('token')
        },
      }).then(response => {
        this.cancel()
        this.$message({
            type: 'success',
            message: '已添加!'
          })
          this.getBike()
      }).catch(error => {
        this.$message({
          type: 'success',
          message: '添加失败!'
        })
        console.log(error)
      })
    },
    handleEdit (index, row) { // -------------------------------------------编辑操作
      this.dialogUpdate = true
      this.ruleForm = Object.assign({}, row) // 这句是关键！！！
    },

    updateBike () { // -----------------------------------------更新操作
      const postData = qs.stringify({
        id: this.ruleForm.id,
        bikeNumber: this.ruleForm.bikeNumber,
        status: this.ruleForm.status,
        longitude: this.ruleForm.longitude,
        latitude: this.ruleForm.latitude,
        createTime: this.ruleForm.createTime,
        lastUpdateTime: this.ruleForm.lastUpdateTime,
        fenceId: this.ruleForm.fenceId
      })
      axios({
        method: 'post',
        url: 'http://localhost:8888/Bike-service/bike/updateBike',
        data: postData,
        headers: {
          token: Cookies.get('token')
        },
      }).then(response => {
        console.log(response)
        this.cancel()
        this.getBike()
        this.$message({
          type: 'success',
          message: '更新成功!'
        })
      }).catch(error => {
        this.$message({
          type: 'success',
          message: '更新失败!'
        })
        console.log(error)
      })
    },

    onSearch () { // --------------------------------------------------查询操作
      
      const postData = qs.stringify({
        bikeNumber: this.search
      })
      axios({
        method: 'post',
        url: 'http://localhost:8888/Bike-service/bike/findBikeByNum',
        data: postData,
        headers: {
          token: Cookies.get('token')
        },
      }).then(response => {
        this.tableData = response.data.bike
        this.disablePage = true
        this.search=''
      }).catch(error => {
        console.log(error)
      })
    },
    getPages () { // -----------------------------------------------------获取页码操作
      this.axios.post('http://localhost:8081/rows').then(response => {
        this.total = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    refreshData () {
      location.reload()
    },
    getBike() {
      // 页面加载时获取所有车辆信息
      const _this = this
    axios({
      url: 'http://localhost:8888/Bike-service/bike/showBike',
      method: 'get',
      headers: {
          token: Cookies.get('token')
        },
    }).then((response) => {
      if (response.data === 'fail') {
        console.log('校验失败')
      } else {
        console.log(response.data.bikeList)
        _this.tableData = response.data.bikeList
      }
    })
    }
    },
   
  };
</script>

<style scoped> 

.search_name{
  width: 200px;
}
.pages{
  margin: 0px;
  padding: 0px;
  text-align: right;
}
.card {
  flex: 1;
}
</style>
  