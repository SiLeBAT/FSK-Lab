#############################################################
#                                                           #
#     QMRA for Salmonella Enteridis in eggs                 #
#                     (outputs) - Moez Sanaa                #
#                                                           #
#############################################################



par(mfrow=c(1,2))

#library(plotly)


# Barplot risk salmo
#y_hbar <- list(title = "Number of illness per million servings of egg")
#plot_ly(
#  x = c("Linghtly eggs", "Well-cooked eggs"),
#  y = c(Rlcuit,Rbcuit),
#  name = "Salmonella risk",
#  type = "bar") %>% layout(xaxis=x_hbar,yaxis=y_hbar)

# alternative
barplot(c(Rlcuit,Rbcuit),names.arg=c("Lightly cooked","Well-cooked"),xlab="Cooking methods of eggs",
        ylab="Number of illness per million servings of egg",ylim=c(0,250),axes = T,col="royalblue")
abline(h=0,col="black")
abline(h=c(50,100,150,200),col="lightgrey")
text(0.5,Rlcuit+5,round(Rlcuit,digits=2))
text(2,Rbcuit+5,round(Rbcuit,digits=2))


# color plot

Age_oeuf=out[,3]
data_oeuf<-cbind(Age_oeuf,Risque)
#x_plot <- list(title = "Egg age (day)")
#y_plot <- list(title = "Probability of illness when ingesting a random serving of egg")
#plot_ly(x = Age_oeuf, y = Risque, mode = "markers", color = Age_oeuf, 
#        size = Age_oeuf)%>%layout(xaxis=x_plot,yaxis=y_plot)

#alternative
#par(mfrow=c(1,1))
plot(x = data_oeuf[which(data_oeuf[,1]<=3),1], y = data_oeuf[which(data_oeuf[,1]<=3),2],
     xlim=c(min(Age_oeuf),max(Age_oeuf)+5), ylim=c(0,max(Risque)),col="yellow",
     xlab="Egg age (day)",ylab="Probability of illness when ingesting a random serving of egg")
points(x = data_oeuf[which(data_oeuf[,1]>3 & data_oeuf[,1]<=7),1],
       y=data_oeuf[which(data_oeuf[,1]>3 & data_oeuf[,1]<=7),2],col="green")
points(x = data_oeuf[which(data_oeuf[,1]>7 & data_oeuf[,1]<=9),1],
       y=data_oeuf[which(data_oeuf[,1]>7 & data_oeuf[,1]<=9),2],col="cyan")
points(x = data_oeuf[which(data_oeuf[,1]>9),1],y=data_oeuf[which(data_oeuf[,1]>9),2],col="royal blue")
i<-(1:16)/4000
rect(max(Age_oeuf)+2,i,max(Age_oeuf)+3,i+0.001,col=rainbow(16,start=1/6,end=4/6))
text(round(max(Age_oeuf)+4,digit=0),min(i),round(min(Age_oeuf),digit=0))
text(round(max(Age_oeuf)+4,digit=0),mean(i),round(mean(Age_oeuf),digit=0))
text(round(max(Age_oeuf)+4,digit=0),max(i),round(max(Age_oeuf),digit=0))







