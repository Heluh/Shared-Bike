<template>
  <cli-layout cur-index="6-1">
    <div style="display: flex; justify-content: center">
      <div style="display: flex; flex-direction: column; min-width: 1000px; width: 70%">
        <!-- 上半part -->
        <div style="display: flex; height: 360px">
          <!-- 新增捐赠活动 -->
          <div @click="openAddDialog">
            <el-card class="add-card">
              <div class="card-bar"/>
              <div class="card-tool-title">新增<br>捐赠<br>活动</div>
              <img class="card-back-icon"
                   alt="card-back-icon"
                   src="http://nas.noneid.life:15730/uploads/big/358c460eb9afc087f1aa570bce34f20f.png"/>
            </el-card>
          </div>

          <!-- 目前开放的捐赠活动 -->
          <el-card class="top-right-table">
            <charity-activity-table ref="nowTable"
                                    :for-now-duration="true"
                                    @updateOppositeListData="updateOppositeListData"
                                    @updateAllList="updateAllList" />
          </el-card>
        </div>

        <!-- 下半part -->
        <div style="display: flex; margin-top: 15px">
          <el-card class="bottom-table">
            <charity-activity-table ref="allTable"
                                    @updateOppositeListData="updateOppositeListData"
                                    @updateAllList="updateAllList" />
          </el-card>
        </div>
      </div>

      <charity-info-dialog type="add" ref="addDialog" @updateAllList="updateAllList" />
    </div>
  </cli-layout>
</template>

<script>
import CliLayout from "~/components/base/CliLayout.vue";
import CharityActivityTable from "~/components/charity/CharityActivityTable.vue";
import CharityInfoDialog from "~/components/charity/CharityInfoDialog.vue";

export default {
  name: "activity",
  components: {CliLayout, CharityActivityTable, CharityInfoDialog},
  methods: {
    openAddDialog() {
      this.$refs.addDialog.showDialog();
    },
    updateOppositeListData(whereFrom) {
      if (whereFrom) {
        this.$refs.allTable.getCharityEventListData();
      } else {
        this.$refs.nowTable.getCharityEventListData();
      }
    },
    updateAllList() {
      this.$refs.allTable.getCharityEventListData();
      this.$refs.nowTable.getCharityEventListData();
    }
  }
}
</script>

<style scoped>
.add-card {
  width: 260px;
  height: 100%;
  position: relative;
  margin-right: 15px;
  cursor: pointer;
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
</style>
