from ffnn import FFNN

import plotly.offline as plotly
import plotly.graph_objs as go

from PIL import Image
import numpy as np
import tensorflow as tf
from sklearn.manifold import TSNE

import glob
import cv2
import test_bounding

#import imageio

plotly.init_notebook_mode(connected=False)

#saver = tf.train.Saver()
#saver.restore(
#    sess, "~/Documents/SBHacks/CircuitBuilder/FeedForwardNeuralNet/session/model.ckpt")
im_list = np.zeros((1, 784), dtype=np.uint8)
print(im_list.shape)
for image_path in glob.glob("/home/ssmarok/Documents/SBHacks/CircuitBuilder/FeedForwardNeuralNet/nn_images/*.png"):
    tempImg = cv2.imread(image_path, cv2.IMREAD_GRAYSCALE)
    print (tempImg.shape)
    print tempImg.dtype
    tempImg = tempImg.reshape(1, tempImg.shape[0] * tempImg.shape[1])
    print (tempImg.shape)
    #tempImg = tempImg.reshape(1, (tempImg.shape[0] * tempImg.shape[1]))
    #im_list.append(tempImg)
    im_list = np.concatenate((im_list, tempImg))
im_list = np.delete(im_list, (0), axis=0)

for i in xrange(10):  # Exponential, don't make too big, for creating multiple test dots
    im_list = np.concatenate((im_list, im_list))

im_list = im_list.astype(float)
for x in range(len(im_list)):
    im_list[x] = abs(255 - im_list[x])
    im_list[x] = im_list[x] / 255.0

print("Shape after {}".format(im_list.shape))

# Load in data as numpy arrays
train_in = np.load('data/train_images_gen.npy')
train_labels = np.load('data/train_labels_gen.npy')
test_in = np.load('data/test_images_gen.npy')
test_labels = np.load('data/test_labels_gen.npy')

print(train_labels.shape[0])
z = np.zeros((train_labels.shape[0], 27), dtype=train_labels.dtype)
train_labels = np.concatenate((train_labels, z), axis=1)
for x in range(len(train_labels)):
    position = train_labels[x][0] - 1  # ADDED -1
    train_labels[x][0] = 0.0
    train_labels[x][position] = 1.0

train_labels = np.delete(train_labels, np.s_[120000:], axis=0)

print(train_labels.shape)

z2 = np.zeros((5120, 28), dtype=train_labels.dtype)

print z2.shape
for x in range(len(z2)):
    z2[x][27] = 1.0

train_labels = np.concatenate((train_labels, z2), axis=0)

train_in = train_in.reshape(
    (train_in.shape[0], train_in.shape[1] * train_in.shape[2]))
train_in = np.concatenate((train_in, im_list))
train_in = train_in.astype(float)
for x in range(len(train_in)):
    train_in[x] = abs(255 - train_in[x])
    train_in[x] = train_in[x] / 255.0

print(train_in.shape)
# Define the neural network structure
# Here we have just one hidden layer with 2048 nodes
network = FFNN([784, 2048, 28], post_function=tf.nn.softmax, session=1)
# Train the model using gradient descent


def cross_entropy_with_softmax(model_output, true_output):
    return tf.reduce_sum(tf.nn.softmax_cross_entropy_with_logits(labels=true_output, logits=model_output))


network.train(
    train_in,
    train_labels,
    epochs=5,   # was 1100
    batch_size=100,
    lc_interval=300,
    loss_func=cross_entropy_with_softmax
)

print("Total epochs: {}".format(network.epochs_global))
network.save_session()
# Lets check our error rate on our train set
train_outputs = network.evaluate(train_in)
print("Correct predictions in train set: {}".format(
    np.sum(train_outputs.argmax(axis=1) == train_labels.argmax(
        axis=1)) / float(train_labels.shape[0])
))

# And check that the predictions match the image
i = 3  # 19
print (train_labels[i])
print("Prediction: {}".format(train_outputs[i].argmax()))
Image.fromarray((train_in[i] * 255).astype('uint8').reshape(28, 28), mode='L')

#Evaluation Model
# Reshape test input
test_in = test_in.reshape(
    (test_in.shape[0], test_in.shape[1] * test_in.shape[2]))
test_in = test_in.astype(float)
for x in range(len(test_in)):
    test_in[x] = abs(255 - test_in[x])
    test_in[x] = test_in[x] / 255.0

# Reshape test labels
z = np.zeros((test_labels.shape[0], 27), dtype=test_labels.dtype)
test_labels = np.concatenate((test_labels, z), axis=1)
for x in range(len(test_labels)):
    position = test_labels[x][0]
    test_labels[x][0] = 0.0
    test_labels[x][position] = 1.0

elmts = test_bounding.process_image('../test_images/test.jpg')
imgs = elmts.values()
seg_list = np.zeros((1, 784), dtype=np.uint8)
for i in xrange(len(elmts)):
    imgs[i] = imgs[i].reshape(1, imgs[i].shape[0] * imgs[i].shape[1])
    seg_list = np.concatenate((seg_list, imgs[i]))
seg_list = np.delete(seg_list, (0), axis=0)
seg_list = seg_list.astype(float)

for x in range(len(seg_list)):
    seg_list[x] = abs(255 - seg_list[x])
    seg_list[x] = seg_list[x] / 255.0

out = []
print seg_list.shape
for i in xrange(len(seg_list)):
    tst = network.evaluate(seg_list[i, None, :])
    print tst.argmax()
    out.append(tst.argmax())

alpha_list = list(map(chr, range(97, 123)))
alpha_list.append('*')
out_str = ''
for i in xrange(len(out)):
    out_str += alpha_list[out[i]]
print out_str
with open('eqn.txt', 'w') as f:
    f.write(out_str)
"""
test_outputs = network.evaluate(test_in)
print("Correct predictions in test_set: {}".format(
    np.sum(test_outputs.argmax(axis=1) == test_labels.argmax(
        axis=1)) / float(test_labels.shape[0])
))

i = 40
print("Prediction: {}".format(test_outputs[i].argmax()))
Image.fromarray((test_in[i] * 255).astype('uint8').reshape(28, 28), mode='L')
"""

#network.save_session()
