from random import randrange


# Note: "base" is the "p" in Z-mod-p... so for exercise 6 it asks you to use
# Z-mod-7, so base would be 7.
class ZMath:
    def __init__(self, base):
        self.m_base = base
        self.m_powerTable = []

        self.m_mults = 0
        self.m_adds = 0
        self.m_pows = 0

    # Convert value into Z-mod-p world
    def toz(self, value):
        return value % self.m_base

    def add(self, val0, val1):
        self.m_adds += 1
        return self.toz(val0 + val1)

    def mult(self, val0, val1):
        self.m_mults += 1
        return self.toz(val0 * val1)

    def pow(self, x, y):
        self.m_pows += 1
        return self.toz(pow(x, y % (self.m_base - 1)))

    def report(self):
        print("m, a, p: " + str(g_mth.m_mults) + ", " + str(g_mth.m_adds) + ", " + str(g_mth.m_pows))


#
# base = (see above)
# alpha = the alpha to generate the matrix with (see ZMatrix.makeAlpha() )
# rows & cols = the matrix dimensions
class ZMatrix:
    def __init__(self, base, alpha, rows, cols=0):
        self.m_base = base
        self.m_alpha = alpha
        self.m_rows = rows
        if cols == 0:
            self.m_cols = rows
        else:
            self.m_cols = cols
        self.m_data = [[0 for i in range(self.m_cols)] for j in range(self.m_rows)]

    # generate a matrix using powers of alpha in Z-mod-p (where p = self.m_base)
    def makeAlpha(self):
        assert(self.m_rows == self.m_cols)

        n = self.m_rows
        a = self.m_alpha

        for i in range(n):
            for j in range(n):
                self.m_data[ i ][ j ] = g_mth.pow(a, i * j)

    # make a 0 matrix with successive powers of alpha
    # in the diagonal
    def makePowersDiag(self):
        assert(self.m_rows == self.m_cols)

        r = self.m_rows

        for i in range(r):
            self.m_data[ i ][ i ] = g_mth.pow(self.m_alpha, i)

    # make a vector out of successive powers of alpha
    def makePowersVec(self):
        assert(self.m_cols == 1)

        r = self.m_rows

        for i in range(r):
            self.m_data[ i ][ 0 ] = g_mth.pow(self.m_alpha, i)

    # makes a list of numbers into a vector
    # (e.g. one column and a bunch of rows)
    def makeVector(self, vec):
        assert(len(vec) == self.m_rows)
        assert(self.m_cols == 1)

        r = self.m_rows

        for i in range(r):
            self.m_data[ i ][ 0 ] = vec[ i ]

    def mult(self, other):
        assert(self.m_cols == other.m_rows)

        r = self.m_rows
        c = other.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(c):
            for j in range(r):
                for k in range(self.m_cols):
                    result.m_data[ j ][ i ] = g_mth.add(
                        result.m_data[ j ][ i ],
                        g_mth.mult(self.m_data[ j ][ k ],
                                   other.m_data[ k ][ i ]))

        return result

    def add(self, other):
        assert(self.m_cols == other.m_cols)
        assert(self.m_rows == other.m_rows)

        r = self.m_rows
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = g_mth.add(self.m_data[ i ][ j ], other.m_data[ i ][ j ])

        return result

    # multiplies each element
    def elemMult(self, other):
        assert(self.m_rows == other.m_rows)
        assert(self.m_cols == other.m_cols)

        r = self.m_rows
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = g_mth.mult(self.m_data[ i ][ j ], other.m_data[ i ][ j ])

        return result

    # attach "other" to the top of this matrix
    def augRight(self, other):
        assert(self.m_rows == other.m_rows)

        r = self.m_rows
        c = self.m_cols + other.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            result.m_data[ i ] = list(self.m_data[ i ])
            result.m_data[ i ].extend(other.m_data[ i ])

        return result

    # attach "other" to the bottom of this matrix
    def augBot(self, other):
        assert(self.m_cols == other.m_cols)

        r = self.m_rows + other.m_rows
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        result.m_data = list(self.m_data)
        result.m_data.extend(other.m_data)

        return result

    def upperLeft(self):
        assert(self.m_cols == self.m_rows)

        r = int(self.m_rows / 2)
        c = int(self.m_cols / 2)
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i ][ j ]

        return result

    def lowerRight(self):
        assert(self.m_cols == self.m_rows)

        r = int(self.m_rows / 2)
        c = int(self.m_cols / 2)
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i + r ][ j + c ]

        return result

    def topHalf(self):
        r = int(self.m_rows / 2)
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i ][ j ]

        return result

    def botHalf(self):
        r = int(self.m_rows / 2)
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i + r ][ j ]

        return result

    def rearrangeCols(self):
        r = self.m_rows
        c = int(self.m_cols / 2)
        result = ZMatrix(self.m_base, self.m_alpha, r, self.m_cols)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i ][ j * 2 ]
                result.m_data[ i ][ j + c ] = self.m_data[ i ][ j * 2 + 1 ]

        return result

    def rearrangeRows(self):
        r = int(self.m_rows / 2)
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, self.m_rows, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = self.m_data[ i * 2 ][ j ]
                result.m_data[ i + r ][ j ] = self.m_data[ i * 2 + 1 ][ j ]

        return result

    def inverse(self):
        r = self.m_rows
        c = self.m_cols
        result = ZMatrix(self.m_base, self.m_alpha, r, c)

        for i in range(r):
            for j in range(c):
                result.m_data[ i ][ j ] = g_mth.pow(self.m_data[ i ][ j ], self.m_base - 2)

        return result

    def show(self):
        r = self.m_rows
        c = self.m_cols

        for i in range(r):
            for j in range(c):
                print(str(self.m_data[ i ][ j ]) + '\t', end='')
            print()

    def equalTo(self, other):
        assert(self.m_rows == other.m_rows)
        assert(self.m_cols == other.m_cols)

        r = self.m_rows
        c = self.m_cols

        for i in range(r):
            for j in range(c):
                if self.m_data[ i ][ j ] != other.m_data[ i ][ j ]:
                    return False

        return True


# m - the matrix to multiply by
# x - a vector matrix (one column, same # of rows as m) full of X values
# 		to multiply by
# alpha - the current alpha being used; only used for generating successive
#		powers of alpha, because when it recurses, alpha becomes beta which
#		is alpha^2
def fft(m: ZMatrix, x: ZMatrix, alpha):
    # FFT call counter; counts recursions
    global g_fftCalls
    g_fftCalls += 1

    # rearrange Xs
    xr = x.rearrangeRows()

    # create V and W
    v = xr.topHalf()
    w = xr.botHalf()

    # rearrange columns in M
    mr = m.rearrangeCols()

    # take upper-left of rearranged M
    ul = mr.upperLeft()

    # create vertex of successive powers of alpha
    dp = ZMatrix(m.m_base, alpha, m.m_rows, 1)
    dp.makePowersVec()
    dpu = dp.topHalf()
    dpl = dp.botHalf()

    # compute FV and FW
    # recurse if N is still even
    if ul.m_rows > 2 and ul.m_rows % 2 == 0:
        fv = fft(ul, v, alpha * alpha)
        fw = fft(ul, w, alpha * alpha)
    else:
        fv = ul.mult(v)
        fw = ul.mult(w)

    # multiply FW by successive powers of alpha
    topr = dpu.elemMult(fw)
    botr = dpl.elemMult(fw)

    # combine FV and transformed FW
    top = fv.add(topr)
    bot = fv.add(botr)

    # augment the top and bottom together and return result
    return top.augBot(bot)


def testBruteForce(base, alpha, xvec):
    global g_mth
    g_mth = ZMath(base)

    m = ZMatrix(base, alpha, base - 1)
    m.makeAlpha()

    print ( "Inverse:" + str (m.inverse().show()))
    x = ZMatrix(base, alpha, base - 1, 1)
    x.makeVector(xvec)

    result = m.mult(x)
    g_mth.report()

    return result


def testFFT(base, alpha, xvec):
    global g_mth
    g_mth = ZMath(base)

    m = ZMatrix(base, alpha, base - 1)
    m.makeAlpha()

    x = ZMatrix(base, alpha, base - 1, 1)
    x.makeVector(xvec)

    result = fft(m, x, alpha)
    g_mth.report()

    return result


def main():
    global g_mth
    global g_fftCalls

    g_fftCalls = 0

    # get base
    base = int(input("Enter base: "))
    g_mth = ZMath(base)

    # get alpha
    alpha = int(input("Enter alpha: "))

    # generate random X's
    xvec = [randrange(1, base - 1) for i in range(base - 1)]
    print("xvc = " + str(xvec))

    # test multiplying matrix with X's using brute force and using FFT
    resultBF = testBruteForce(base, alpha, xvec)
    resultFFT = testFFT(base, alpha, xvec)

    # check if results are equal
    if resultBF.equalTo(resultFFT):
        print("Test passed!")
    else:
        print("Test failed.")

    # show results
    print(str (resultFFT.show()))
    print(str(resultFFT.m_data))
    print("FFT Calls: " + str(g_fftCalls))


if __name__ == "__main__":
    main()
