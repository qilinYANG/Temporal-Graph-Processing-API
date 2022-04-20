import matplotlib.pyplot as plt 
import numpy as np


def readFileToList(filename):
	result = []

	with open(filename, 'r') as f:
		lines = f.readlines()
		# convert lines to lists
		for line in lines:
			line = line.strip().split(',')
			result.append(list(map(int, line)))

	return result


def plotGraph(result):
	x = result[:, 1]
	y = result[:, 2]

	plt.figure(figsize=(15,7))
	plt.plot(x, y, 'b')
	plt.xlabel('number of neighbors added')
	plt.ylabel('latency (in millisecond)')
	plt.savefig('add_edge_latency.png')


if __name__ == "__main__":
	filename = "latencyTest.txt"
	result = readFileToList(filename)
	result = np.array(result)
	plotGraph(result)