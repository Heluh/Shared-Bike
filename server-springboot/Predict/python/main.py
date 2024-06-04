import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler
import tensorflow as tf
import sys

# 用来接收参数 0:py文件名 1:CSV文件,2: 模型的类别
javaToPy = sys.argv[0:]

# csvPath = "D:\\Program\\workspace\\python\\实训材料\\fallraw_7041JA26clear.csv" # 数据文件
# 加载数据集
pre_len = 24 * 2+24
df = pd.read_csv(javaToPy[1], header=0, index_col=0)
# df = pd.read_csv(csvPath, header=0, index_col=0)
df = df.iloc[-pre_len:-24,:]
# 数据处理
x_pre_set = df.iloc[:, :-1].copy()
y_pre_set = df.iloc[:, -1:].copy()

sc1 = MinMaxScaler(feature_range=(0, 1))
x_set = sc1.fit_transform(x_pre_set)

sc2 = MinMaxScaler(feature_range=(0, 1))
y_set = sc2.fit_transform(y_pre_set)


# 多维预测分块--------------------------------------------------
def data_sblocck(x_set, sblock):
    x_train = []
    for i in range(sblock, len(x_set)):
        x_train.append(x_set[i - sblock:i, :])

    x_train = np.array(x_train)
    return x_train


sblock = 2 * 24  # 训练的步数   小时数
x_pre = data_sblocck(x_set, sblock)

# 加载模型
if javaToPy[2] == '单层LSTM':  # 单层lstm 模型
    model = tf.keras.models.load_model(r'D:\shixun\final\server-springboot\\Predict\\python\\model\\1lstm.h5')
elif javaToPy[2] == '双层LSTM':  # 双层lstm 模型
    model = tf.keras.models.load_model(r'D:\shixun\final\server-springboot\\Predict\\python\\model\\2lstm.h5')
elif javaToPy[2] == 'GRU':  # GRU 模型
    model = tf.keras.models.load_model(r'D:\shixun\final\server-springboot\\Predict\\python\\model\\GRU.h5')
elif javaToPy[2] == 'simpleRNN':  # SimpleRNN 模型
    model = tf.keras.models.load_model(r'D:\shixun\final\server-springboot\\Predict\\python\\model\\simpleRNN.h5')
else:
    model = tf.keras.models.load_model(r'E:\\实训04\\server-springboot\\Predict\\python\\model\\BiRNN.h5')

# model = tf.keras.models.load_model(r'D:\Program\workspace\java\Idea\water levels\python\model\lstm.h5')

# model.summary() # 模型结构

# 数据预测
y_res = model.predict(x_pre)
y_result = sc2.inverse_transform(y_res)
y_result = y_result.reshape(1, len(y_result))
# y_result  为 预测数据
print(y_result)
