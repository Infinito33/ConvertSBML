%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Stochastic simulations of P53|MDM2 pathway          %
%   Plots                                               %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

figure(1)
set(gcf,'Color',[1,1,1])

subplot(4,3,1)
plot(T,Yav(:,5))
grid on;
title('[A], PTEN');

subplot(4,3,2)
plot(T,Yav(:,1))
grid on;
title('[B], PIPp');

subplot(4,3,3)
plot(T,Yav(:,2))
grid on;
title('[C], AKTp');

subplot(4,3,4)
plot(T,Yav(:,3))
grid on;
title('[D], MDM2');

subplot(4,3,5)
plot(T,Yav(:,4))
grid on;
title('[E], MDM2p');

subplot(4,3,6)
plot(T,Yav(:,6))
grid on;
title('[F], MDM2pn');

subplot(4,3,7)
plot(T,Yav(:,7))
grid on;
title('[G], P53n');

subplot(4,3,8)
plot(T,Yav(:,8))
grid on;
title('[H], P53pn');

subplot(4,3,9)
plot(T,Yav(:,10))
grid on;
title('[I], PTEN mRNA');

subplot(4,3,10)
plot(T,Yav(:,9))
grid on;
title('[J], MDM2 mRNA');

subplot(4,3,11)
plot(T,Amdm2AV)
grid on;
title('[K], MDM2 gene state');

subplot(4,3,12)
plot(T,AptenAV)
grid on;
title('[L], PTEN gene state');

%#####################################################################

figure(2)
set(gcf,'Color',[1,1,1])

plot(T,Yav(:,8),'g')
hold on
plot(T,Yav(:,6),'r')
plot(T,1000*NBAV,'b')
hold off
grid on
legend('nuclear p53p','nuclear Mdm2','DNA damage*1000')
title('nuclear p53p vs nuclear Mdm2 and DNA damage level');
xlabel('Time in hours'); 

%#####################################################################

figure(3)
set(gcf,'Color',[1,1,1])

subplot(3,1,1)
plot(T,Amdm2AV)
grid on;
title('[K], MDM2 gene state');

subplot(3,1,2)
plot(T,Yav(:,9))
grid on;
title('[J], MDM2 mRNA');

subplot(3,1,3)
plot(T,Yav(:,6))
grid on;
title('[F], MDM2pn');

%########################################################################

figure(4)
set(gcf,'Color',[1,1,1])
subplot(2,1,1)
plot(T,Yav(:,11),'g')
title('[A], Apoptotic factors');

subplot(2,1,2)
plot(T,NBAV,'r')
title('[B], DNA damage level');

%########################################################################

figure(5)
plot(Scatter(:,1),Scatter(:,2),'.r','LineWidth',2)
hold on
plot([0 6*10^5],[0 3*10^5],'b','LineWidth',2)

set(gca,'fontsize',15,'fontweight','bold')
xlabel('MDM2pn','fontsize',15,'fontweight','bold')
ylabel('P53pn','fontsize',15,'fontweight','bold')
title('Cell Fate','fontsize',15,'fontweight','bold')
axis([0 5*10^5 0 3*10^5])

Apoptotic=find(Scatter(:,1)<2*Scatter(:,2));
Na=size(Apoptotic,1);

annotation(gcf,'textbox','String',{['Apoptotic (' num2str(Na) ')']},'FitBoxToText','on',...
    'linestyle','none','fontsize',15,'fontweight','bold','Position',[0.78 0.8 0.15 0.04]);
annotation(gcf,'textbox','String',{['Surviving (' num2str(N-Na) ')']},'FitBoxToText','on',...
    'linestyle','none','fontsize',15,'fontweight','bold','Position',[0.78 0.6 0.15 0.04]);
hold off