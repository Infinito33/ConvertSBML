%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.03.2015                     %
%   CCmodel plots                                       %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% y0(1)=188300; %APIP3
% y0(2)=1234; %AAKT
% y0(3)=57990; %MDM2
% y0(4)=16345; %MDM2P
% y0(5)=81218; %PTEN
% y(6) MDM2N
% y0(7)=15976; %P53
% y(8) P53P
% y(9) MDM2t
% y(10) PTENt
% y(11) P53t
% y(12) GMDM2
% y(13) GPTEN
% y(14) GP53
% y0(15)=0; %IMDM2
% y0(16)=0; %IMDM2P
% y0(17)=0; %IMDM2N
% y(18) NUT
% y0(19)=1353; %P53MDM2N
% y0(20)=148; %P53PMDM2N
% y0(21)=278; %P53UMDM2N
% y0(22)=3; %P53PUMDM2N
% y0(23)=3286; %P53U
% y0(24)=400; %P53PU
% y0(25)=451; %P53UU
% y0(26)=6; %P53PUU
% y(27) NUTET
% y(28) AF
% y0(29)=0; %TIME

figure(1) % p53 Core

set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,Y(:,13))
grid on
title('Pten gene')

subplot(3,3,4)
plot(T,Y(:,10))
grid on
title('Pten transcript')

subplot(3,3,7)
plot(T,Y(:,5))
grid on
title('Pten')

subplot(3,3,2)
plot(T,Y(:,12))
hold on
grid on
title('Mdm2 gene')

subplot(3,3,5)
plot(T,Y(:,9))
grid on
title('Mdm2 transcript')

subplot(3,3,8)
plot(T,Y(:,6))
grid on
title('Active Nuclear Mdm2')

subplot(3,3,3)
plot(T,Y(:,14))
grid on
title('p53 gene')

subplot(3,3,6)
plot(T,Y(:,11))
grid on
title('p53 transcript')

subplot(3,3,9)
plot(T,Y(:,8))
grid on
title('Phospho-p53')

%#################################################

figure(2) %Inputs
set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,Y(:,27))
grid on;
title('External Nutlin (Zhang Case)')

subplot(3,3,4)
plot(T,Y(:,18))
grid on;
title('Internal free Nutlin')

subplot(3,3,7)
plot(T,Y(:,15)+Y(:,16)+Y(:,17)+Y(:,18))
grid on;
title('Internal total Nutlin')

subplot(3,3,2)
plot(T,Y(:,30))
grid on;
title('DSB')

subplot(3,3,5)
plot(T,Y(:,28))
grid on
title('Apoptotic Factor')

subplot(3,3,8)
plot(T,Y(:,31))
grid on
title('siRNA Mdm2')

subplot(3,3,3)
plot(T,Y(:,32))
grid on;
title('siRNA PTEN');

subplot(3,3,6)
plot(T,Y(:,1))
grid on;
title('CCB countdown');

subplot(3,3,9)
plot(T,Y(:,1))
grid on;
title('CCB decision');

% %#################################################
% 
% figure(3)
% set(gcf,'Color',[1,1,1])
% 
% plot(T,Y(:,10),'g')
% hold on
% plot(T,Y(:,8),'r')
% plot(T,1000*Y(:,15),'b')
% hold off
% grid on;
% legend('53pn','MDM2n','1000*DNAdam')
% title('active p53, nuclear MDM2 and DNA damage level')
% %axis([0 T(size(T,1)) 0 5*10^5])
% 
% %#################################################
% 
% figure (4)
% set(gcf,'Color',[1,1,1])
% 
% subplot(3,3,1)
% plot(T,Y(:,14))
% grid on
% title('Apoptotic Factor')
% 
% subplot(3,3,2)
% plot(T,Y(:,5))
% grid on
% title('Cytoplasmic Mdm2')
% 
% subplot(3,3,3)
% plot(T,Y(:,19))
% grid on
% title('Cytoplasmic inactive Mdm2')
% 
% subplot(3,3,4)
% plot(T,Y(:,9))
% grid on
% title('Nuclear p53')
% 
% subplot(3,3,5)
% plot(T,Y(:,6))
% grid on
% title('Cytoplasmic phospho-Mdm2')
% 
% subplot(3,3,6)
% plot(T,Y(:,20))
% grid on
% title('Cytoplasmic inactive phospho-Mdm2')
% 
% subplot(3,3,7)
% plot(T,Y(:,10))
% grid on
% title('Nuclear phospho-p53')
% 
% subplot(3,3,8)
% plot(T,Y(:,8))
% grid on
% title('Nuclear phospho-Mdm2')
% 
% subplot(3,3,9)
% plot(T,Y(:,21))
% grid on
% title('Nuclear inactive phospho-Mdm2')
