import shelve
my_shelf = shelve.open(filename, 'n')
for key in dir():
    try:
        my_shelf[key] = globals()[key]
    except TypeError:
        print('ERROR shelving: {0}'.format(key))
    except AttributeError:
        print('ERROR shelving: {0}'.format(key))
my_shelf.close()
