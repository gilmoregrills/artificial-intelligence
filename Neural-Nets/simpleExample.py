import numpy as np

def nonlin(x, deriv = False):
    if(deriv == True):
        return x * (1 - x)
    return 1 / (1 + np.exp(-x))

inputs = np.array([  [0, 0, 1, 0],
                    [0, 0, 1, 1],
                    [1, 0, 1, 0],
                    [2, 1, 1, 0],
                    [2, 1, 0, 0],
                    [2, 1, 0, 1],
                    [1, 1, 0, 1],
                    [0, 2, 1, 0],
                    [0, 1, 0, 0],
                    [2, 2, 0, 1],
                    [1, 2, 1, 1],
                    [1, 1, 0, 0],
                    [2, 2, 1, 1],
                    ])

output = np.array( [[0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0]]).T

np.random.seed(1)

weights0 = 2*np.random.random((4, 14)) - 1
weights1 = 2*np.random.random((14, 1)) - 1

print "weights"
print weights0
print weights1

for iter in xrange(60000):

    l0 = inputs
    l1 = nonlin(np.dot(l0, weights0))
    l2 = nonlin(np.dot(l1, weights1))

    l2_error = output - l2

    l2_delta = l2_error * nonlin(l2, deriv=True)

    l1_error = l2_delta.dot(weights1.T)

    l1_delta = l1_error * nonlin(l1, deriv=True)

    weights1 += l1.T.dot(l2_delta)
    weights0 += l0.T.dot(l1_delta)


print "Output after training:"
print l2
