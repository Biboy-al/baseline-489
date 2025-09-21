import pandas as pd
import matplotlib.pyplot as plt

stat = pd.read_csv("../src/project489/stats/outtabular_Basline.stat", sep=" ")

print(stat)

plt.figure(figsize=(10,6) )

plt.plot(stat["generation"], stat["bestOfGenFitness"])

plt.show()
