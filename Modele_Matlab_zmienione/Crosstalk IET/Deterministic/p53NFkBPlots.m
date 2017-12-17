%#############################################
%                                            #
% p53-NFkB deterministic simulations - plots #
%                                            #
% KP 02.03.2009                              #
%                                            #
%#############################################

% dy(1)  % inactive PIP3 (PIP2)
% dy(2)  % active PIP3
% dy(3)  % inactive cytoplasmic AKT
% dy(4)  % active cytoplasmic AKT
% dy(5)  % cytoplasmic MDM2
% dy(6)  % cytoplasmic phospho-MDM2
% dy(7)  % cytoplasmic PTEN
% dy(8)  % nuclear phospho-MDM2
% dy(9)  % nuclear P53
% dy(10) % nuclear phospho-P53 (P53p)
% dy(11) % MDM2 transcript
% dy(12) % PTEN transcript
% dy(13) % Apoptotic factors
% 
% dy(14) %inactive IKKK
% dy(15) %active IKKK kinase 
% dy(16) %neutral IKK   
% dy(17) %free active IKK                                                                                    
% dy(18) %inactive IKK (IKKi)
% dy(19) %second inactive IKK (IKKii)
% dy(20) %Phospo-IkBa cytoplasmic 
% dy(21) %cytoplasmic (phospho-IkBa|NF-kB) 
% dy(22) %free cytoplasmic NFkB
% dy(23) %free nuclear NFkB
% dy(24) %cytoplasmic A20
% dy(25) %A20 transcript
% dy(26) %free cytoplasmic IkBa
% dy(27) %free nuclear IkBan
% dy(28) %IkBa transcript
% dy(29) %cytoplasmic (IkBa|NFkB) complex
% dy(30) %nuclear (IkBa|NFkB) complex
% 
% dy(31) %p53 transcript
% dy(32) %DNA damage level 
% dy(33) %Active receptors
% 
% dy(34) %Mdm2 gene state
% dy(35) %PTEN gene state
% dy(36) %p53 gene state
% dy(37) %A20 gene state
% dy(38) %IkBa gene state

figure(1)

set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,Y(:,12))
hold on
grid on
title('Pten transcript')

subplot(3,3,2)
plot(T,Y(:,7))
hold on
grid on
title('Pten')

subplot(3,3,3)
plot(T,Y(:,2))
hold on
grid on;
title('Active PIP');

subplot(3,3,4)
plot(T,Y(:,4))
hold on
grid on;
title('Active Act');

subplot(3,3,5)
plot(T,Y(:,28))
hold on
grid on;
title('IkBa transcript');

subplot(3,3,6)
plot(T,Y(:,29))
grid on;
title('cytoplasmic (IkBa|NFkB)');

subplot(3,3,7)
plot(T,Y(:,30))
hold on
grid on;
title('nuclear (IkBa|NFkB)'); 

subplot(3,3,8)
plot(T,Y(:,27))
hold on
grid on;
title('free nuclear IkBan'); 

subplot(3,3,9)
plot(T,Y(:,26))
hold on
grid on;
title('free cytoplasmic IkBa'); 

%#################################################

figure(2)
set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,Y(:,10))
hold on
grid on;
title('active p53')

subplot(3,3,2)
plot(T,Y(:,8))
hold on
grid on;
title('nuclear MDM2')

subplot(3,3,3)
hold on
plot(T,1000*Y(:,32))
grid on;
title('DNA damage level')


subplot(3,3,4)
plot(T,Y(:,23))
hold on
grid on;
title('nuclear NFkB');

subplot(3,3,5)
plot(T,Y(:,13))
hold on
grid on
title('Apoptotic Factor')

subplot(3,3,6)
plot(T,Y(:,31))
hold on
grid on;
title('p53 mRNA');

subplot(3,3,7)
plot(T,Y(:,10)+Y(:,9))
hold on
grid on;
title('total p53')

subplot(3,3,8)
plot(T,Y(:,4))
hold on
grid on;
title('Active Act');

subplot(3,3,9)
plot(T,Y(:,24))
hold on
grid on;
title('A20');

%#################################################

figure(3)
set(gcf,'Color',[1,1,1])

subplot(1,2,1)
plot(T,Y(:,10),'g')
hold on
plot(T,Y(:,8),'r')
plot(T,1000*Y(:,32),'b')
grid on;
legend('53pn','MDM2n','1000*DNAdam')
title('active p53, nuclear MDM2 and DNA damage level')

subplot(1,2,2)
plot(T,Y(:,23),'g')
hold on
plot(T,Y(:,27),'r')
grid on;
legend('NFkBn','IkBan')
title('nuclear NFkB and nuclear IkBa');


figure (4)

plot(T,Y(:,13))
hold on
grid on
title('Apoptotic Factor')