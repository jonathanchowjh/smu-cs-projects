# Name: Hartono Tjakrawinata @ Jonathan Chow
# Email ID: hartonot.2019@sis.smu.edu.sg
import heapq
import pickle
##############################################################
# encode
##############################################################
# Calc Frequencies
# Create Nodes
# Add into tree in increasing order
# Create a Translation table
# Encode data
def encode(filename):
    HT = Huffmans_Tree(filename)
    return HT.encode()

##############################################################
# decode
##############################################################
# Init Huffman's Tree
# Decode
def decode(filename):
    HT = Huffmans_Tree(filename)
    return HT.decode()

##############################################################

def save_pickle(obj, name):
    with open(name + '.metadata', 'wb') as f:
        pickle.dump(obj, f, pickle.HIGHEST_PROTOCOL)

def load_pickle(name):
    with open(name + '.metadata', 'rb') as f:
        return pickle.load(f)

class Huffmans_Tree:
    def __init__(self, filename):
        self.filename = filename
        self.root = None
    
    def decode(self):
        # READ ENCODED DATA FROM FILE
        encoded_content = ""
        original_content = ""
        with open(self.filename + '.payload', 'rb') as file:
            encoded_content = bin(int.from_bytes(file.read(), byteorder='big'))
        encoded_content = encoded_content[2:]

        # READ TREE FROM FILE
        frequencies = load_pickle(self.filename)
        self.root = self.create_tree(frequencies)
        curr = self.root

        # TRAVERSE TREE
        for ch in encoded_content:
            if ch == '0':
                curr = curr.l
            elif ch == '1':
                curr = curr.r
            else:
                return None
            if curr.ch is not None:
                original_content += curr.ch
                curr = self.root
        
        # REMOVE FIRST CHAR
        original_content = original_content[1:]
        # SAVE
        with open(self.filename + '_unzipped.txt', 'w') as file:
            file.write(original_content)
        return original_content

    def encode(self):
        # CREATE TREE
        frequencies = self.create_freq()
        self.root = self.create_tree(frequencies)
        # CREATE TRANSLATION TABLE
        trans = {}
        self.create_trans(self.root, trans, "")
        # ENCODING
        original_file = open(self.filename + '.txt', "r")
        original_content = original_file.read()
        encoded_content = ""
        for ch in original_content:
            encoded_content += trans.get(ch, '')
        # ADD ANY CHAR WOUT LEADING ZERO
        for ch in trans.items():
            if ch[1][0] == '1':
                encoded_content = ch[1] + encoded_content
                break
        # SAVE
        bit_length = len(encoded_content)
        byte_length = bit_length // 8 + 1
        with open(self.filename + '.payload', 'wb') as file:
            file.write((int(encoded_content, 2)).to_bytes(byte_length, byteorder='big', signed=False))
        save_pickle(frequencies, self.filename)
        # print(original_content)
        # print(trans)
        # print(encoded_content)
        return encoded_content

    # CREATE TREE
    def create_tree(self, frequencies):
        if frequencies is None: return None
        heap = []
        # INSERT INTO HEAP
        for item in frequencies.items():
            node = H_Node(item[0], item[1], None, None)
            heapq.heappush(heap, node)
        # REMOVE FROM HEAP
        while len(heap) > 1:
            x, y = heapq.heappop(heap), heapq.heappop(heap)
            node = H_Node(None, x.freq + y.freq, x, y)
            heapq.heappush(heap, node)
        return heap[0]

    # CALC FREQUENCIES
    def create_freq(self):
        if self.filename is None: return None
        frequencies = dict()
        original_file = open(self.filename + '.txt', "r")
        original_content = original_file.read()
        for ch in original_content:
            frequencies[ch] = frequencies.get(ch, 1) + 1
        return frequencies
    
    # CREATE TRANSLATION TABLE
    def create_trans(self, root, memo, curr):
        if root is None: return
        if root.ch is not None:
            memo[root.ch] = curr
        self.create_trans(root.l, memo, curr + '0')
        self.create_trans(root.r, memo, curr + '1')

class H_Node:
    def __init__(self, ch, freq, l, r):
        self.ch = ch
        self.freq = freq
        self.l = l
        self.r = r
    
    def __repr__(self):
        return self.__str__()
    def __str__(self):
        return "" + str(self.freq) + str(self.ch)
    
    def __eq__(self, other):
        if isinstance(other, H_Node):
            return (self.freq - other.freq) == 0
        return None
    def __ge__(self, other):
        if isinstance(other, H_Node):
            return (self.freq - other.freq) >= 0
        return None
    def __gt__(self, other):
        if isinstance(other, H_Node):
            return (self.freq - other.freq) > 0
        return None
    def __le__(self, other):
        if isinstance(other, H_Node):
            return (self.freq - other.freq) <= 0
        return None
    def __lt__(self, other):
        if isinstance(other, H_Node):
            return (self.freq - other.freq) < 0
        return None