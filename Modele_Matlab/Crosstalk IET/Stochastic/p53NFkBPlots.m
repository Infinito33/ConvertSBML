%##########################################
%                                         #
% p53-NFkB stochastic simulations - plots #
%                                         #
% KP 02.03.2009                           #
%                                         #
%##########################################

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
%
% NB %DNA damage level 
% B %Active receptors
% 
% Amdm2 %Mdm2 gene state
% Apten %PTEN gene state
% Ap53 %p53 gene state
% Aa20 %A20 gene state
% Aikba %IkBa gene state

%#################################################
figure(1)
set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,YAV(:,12))
hold on
grid on
title('Pten transcript')

subplot(3,3,2)
plot(T,YAV(:,7))
hold on
grid on
title('Pten')

subplot(3,3,3)
plot(T,YAV(:,2))
hold on
grid on;
title('Active PIP');

subplot(3,3,4)
plot(T,YAV(:,4))
hold on
grid on;
title('Active Act');

subplot(3,3,5)
plot(T,YAV(:,28))
hold on
grid on;
title('IkBa transcript');

subplot(3,3,6)
plot(T,YAV(:,29))
grid on;
title('cytoplasmic (IkBa|NFkB)');

subplot(3,3,7)
plot(T,YAV(:,30))
hold on
grid on;
title('nuclear (IkBa|NFkB)'); 

subplot(3,3,8)
plot(T,YAV(:,27))
hold on
grid on;
title('free nuclear IkBan'); 

subplot(3,3,9)
plot(T,YAV(:,26))
hold on
grid on;
title('free cytoplasmic IkBa'); 

%#################################################
figure(2)
set(gcf,'Color',[1,1,1])

subplot(3,3,1)
plot(T,YAV(:,10))
hold on
grid on;
title('active p53')

subplot(3,3,2)
plot(T,YAV(:,8))
hold on
grid on;
title('nuclear MDM2')

subplot(3,3,3)
hold on
plot(T,1000*NBAV)
grid on;
title('DNA damage level')

subplot(3,3,4)
plot(T,YAV(:,23))
hold on
grid on;
title('nuclear NFkB');

subplot(3,3,5)
plot(T,YAV(:,13))
hold on
grid on
title('Apoptotic Factor')

subplot(3,3,6)
plot(T,YAV(:,31))
hold on
grid on;
title('p53 mRNA');

subplot(3,3,7)
plot(T,YAV(:,10)+YAV(:,9))
hold on
grid on;
title('total p53')

subplot(3,3,8)
plot(T,YAV(:,4))
hold on
grid on;
title('Active Act');

subplot(3,3,9)
plot(T,YAV(:,24))
hold on
grid on;
title('A20');

%#################################################
figure(3)
set(gcf,'Color',[1,1,1])

subplot(1,2,1)
plot(T,YAV(:,10),'g')
hold on
plot(T,YAV(:,8),'r')
plot(T,1000*NBAV,'b')
grid on;
legend('53pn','MDM2n','1000*DNAdam')
title('active p53, nuclear MDM2 and DNA damage level')

subplot(1,2,2)
plot(T,YAV(:,23),'g')
hold on
plot(T,YAV(:,27),'r')
grid on;
legend('NFkBn','IkBan')
title('nuclear NFkB and nuclear IkBa');

%#################################################
figure (4)
set(gcf,'Color',[1,1,1])

plot(T,YAV(:,13))
hold on
grid on
title('Apoptotic Factor')

%#######################################################################
% Scatter: AKTp as marker

Nc=size(Scatter,1);

figure(5)
plot(Scatter(:,1),Scatter(:,2),'.r','LineWidth',2)
hold on
plot([0 6*10^4],[0 9*10^5],'b','LineWidth',2)

set(gca,'fontsize',15,'fontweight','bold')
xlabel('AKTp','fontsize',15,'fontweight','bold')
ylabel('P53pn','fontsize',15,'fontweight','bold')
title('Cell Fate','fontsize',15,'fontweight','bold')
axis([0 6*10^4 0 9*10^5])

Apoptotic=find(6*Scatter(:,1)<Scatter(:,2));
Na=size(Apoptotic,1);

annotation(gcf,'textbox','String',{['Apoptotic (' num2str(Na) ')']},'FitBoxToText','on',...
    'linestyle','none','fontsize',20,'fontweight','bold','Position',[0.4 0.852 0.15 0.04]);
annotation(gcf,'textbox','String',{['Surviving (' num2str(Nc-Na) ')']},'FitBoxToText','on',...
    'linestyle','none','fontsize',20,'fontweight','bold','Position',[0.6 0.55 0.15 0.04]);
hold off