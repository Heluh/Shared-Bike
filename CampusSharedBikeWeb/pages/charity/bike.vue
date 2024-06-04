<template>
  <cli-layout cur-index="6-2">
    <div style="display: flex; justify-content: center">
      <div style="display: flex; flex-direction: column; min-width: 1000px; width: 70%">
        <!-- 上半part -->
        <div style="display: flex; height: 360px">
          <!-- 车辆录入展示块 -->
          <el-card class="add-card">
            <div class="card-bar"/>
            <div class="card-tool-title">录入<br>车辆</div>
            <div class="card-right-cursor"> >> </div>
            <img class="card-back-icon"
                 alt="card-back-icon"
                 src="http://nas.noneid.life:15730/uploads/big/358c460eb9afc087f1aa570bce34f20f.png"/>
          </el-card>

          <!-- 目前开放的捐赠活动 -->
          <el-card class="top-right-table">
            <charity-bike-in-form @updateAllList="updateAllList" />
          </el-card>
        </div>

        <!-- 下半part -->
        <div style="display: flex; margin-top: 15px">
          <el-card class="bottom-table">
            <el-tabs v-model="activeTableName" type="card">
              <el-tab-pane label="入库未投入运营" name="1">
                <charity-bike-table ref="inBikeTable" @updateAllList="updateAllList" />
              </el-tab-pane>
              <el-tab-pane label="已投入运营" name="2">
                <charity-bike-table type="bind" ref="bindBikeTable"
                                    @updateAllList="updateAllList" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </div>
    </div>
  </cli-layout>
</template>

<script>
import CliLayout from "~/components/base/CliLayout.vue";
import CharityBikeInForm from "~/components/charity/CharityBikeInForm.vue";
import CharityBikeTable from "~/components/charity/CharityBikeTable.vue";

export default {
  name: "bike",
  components: {CliLayout, CharityBikeInForm, CharityBikeTable},
  data() {
    return {
      activeTableName: '1',
    }
  },
  methods: {
    updateAllList() {
      this.$refs.inBikeTable.getCharityBikeListData();
      this.$refs.bindBikeTable.getCharityBikeListData();
    },
  },
}
</script>

<style scoped>
.add-card {
  width: 260px;
  height: 100%;
  position: relative;
  margin-right: 15px;
}

>>> .add-card .el-card__body {
  padding: 0;
  height: 100%;
}

.card-tool-title {
  position: absolute;
  top: 20%;
  left: 20%;
  transform: translate(-20%, -20%);
  font-size: 50px;
  font-weight: bold;
  color: #2a5ad7;
  text-align: center;
}

.card-right-cursor {
  position: absolute;
  top: 2%;
  right: 5%;
  transform: translate(5%, -2%);
  font-size: 40px;
  color: #2a5ad7;
  text-align: center;
  opacity: 0.8;
}

.card-bar {
  width: 100%;
  height: 10px;
  background-color: #2a5ad7;
}

.card-back-icon {
  position: absolute;
  bottom: 18px;
  right: -4px;
  width: 170px;
  height: 170px;
  opacity: 0.2;
}

.top-right-table {
  flex: 1;
}

.bottom-table {
  flex: 1;
}

::v-deep .el-tabs__item:hover {
  color: #000;
}

::v-deep .el-tabs__item.is-active {
  color: #fff;
  background-color: #409eff;
}
</style>
