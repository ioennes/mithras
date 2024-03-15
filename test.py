import tensorflow as tf
from tensorflow.keras import models, layers
from sklearn.tree import *
from sklearn.svm import *
from sklearn.model_selection import train_test_split
from sklearn.metrics import *
from sklearn.model_selection import KFold

dnn1 = Sequential([
	layers.Input(shape=(128, 128, 3), sparse=False),
	layers.Dense(units=32, trainable=True, use_bias=True, dtype="float32")
])

dnn1.fit(x=train, epochs=0, batch_size=0, validation_split=0.0)

dnn2 = Sequential([
	layers.Input(shape=(128, 128, 5), sparse=False),
	layers.Dense(units=3, trainable=True, use_bias=True, dtype="float32")
])

dnn2.fit(x=train, epochs=0, batch_size=0, validation_split=0.0)

svm1 = LinearSVC(penalty="l2", loss="squared_hinge", dual=True, tol=1.0E-4, C=1.0, multi_class="ovr", fit_intercept=True, intercept_scaling=1.0, max_iter=1000)


