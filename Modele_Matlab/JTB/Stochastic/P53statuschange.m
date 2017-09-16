%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Changes the values of the discrete variables        %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function [Tchange,NB,Amdm2,Apten]=P53statuschange(IR,te,p53pX,NAmdm2,NApten,NBX,Tbreaks,Amdm2X,AptenX,dt,DNASw,AFX,ExtSw);

[a6 q3 d9 p1 a0 a1 a2 a3 a4 a5 c0 c1 c2 c3 p0 s0 s1 t0 t1 d0 d1 d2 d3 d4 d5 d6 d7 d8 i0 e0 h0 h1 n0 n1 AKTtot PIPtot drep q0 q0M q0P q1 q2 NSAT]=P53parametersS(te,DNASw,ExtSw);

PA=(q0+q1*p53pX.^n1)./(q2+q0+q1*p53pX.^n1); % transcriptional efficiency of p53 dependent genes 

NB=NBX;
Amdm2=Amdm2X;
Apten=AptenX;

ro=Tbreaks*IR+a6*(AFX*10^-6).^4+(NB*drep*PA)./(NB+NSAT*PA)+(NAmdm2-Amdm2)*(q1*p53pX.^n1+q0M)+Amdm2*q2+(NApten-Apten)*(q1*p53pX.^n1+q0P)+Apten*q2; %total propensity function
roint=dt*cumtrapz(ro);        % propensity function integrated
fd=1-exp(-roint);             % Distribution of the switching time

r=rand;                     
if (fd(length(fd))<r)
    fprintf('Error. One may need to increase time interval for computing Risk Function\n');
    fprintf('since r > Risk Function at the end of the time interval.\n');
    fprintf('Risk function attains value %2.10f.\n',fd(length(fd)));  %warning waiting time to short    
end;

a=abs(fd-r);
Tchange=find((a-min(a))==0,1);   % Tchange = index (time) when the status changes
clear a fd ro roint;          

%##############################################################
%############### WHERE is the change  #########################
%##############################################################

PPA=(q0+q1*(p53pX(Tchange))^n1)/(q2+q0+q1*(p53pX(Tchange))^n1);  % PPA=PA(Tchange)

paDNA=Tbreaks*IR+a6*(AFX(Tchange)*10^-6)^4;          % risk of one more DSB occurs at time TC
pdaDNA=(NB*drep*PPA)/(NB+NSAT*PPA);                  % risk of one DSB repairs at time TC
paMdm2=(NAmdm2-Amdm2)*(q1*(p53pX(Tchange))^n1+q0M);  % risk of activation of Mdm2 site at time TC
pdaMdm2=Amdm2*q2;                                    % risk of inactivation of Mdm2 site at time TC
paPTEN=(NApten-Apten)*(q1*(p53pX(Tchange))^n1+q0P);  % risk of activation of PTEN site at time TC
pdaPTEN=Apten*q2;                                    % risk of inactivation of PTEN site at time TC   

ss=(paDNA+pdaDNA+paMdm2+pdaMdm2+paPTEN+pdaPTEN);

paDNA=paDNA/ss;
pdaDNA=pdaDNA/ss;
paMdm2=paMdm2/ss;
pdaMdm2=pdaMdm2/ss;
paPTEN=paPTEN/ss;
pdaPTEN=pdaPTEN/ss;

rnumber=rand;
if (rnumber<paDNA)                                                                                     NB=NB+1; end;   %DNA damage
if (rnumber>=paDNA)&(rnumber<paDNA+pdaDNA)                                                             NB=NB-1; end;   %DNa repair 
if (rnumber>=paDNA+pdaDNA)&(rnumber<paDNA+pdaDNA+paMdm2)                                               Amdm2=Amdm2+1; end; %Mdm2 activation
if (rnumber>=paDNA+pdaDNA+paMdm2)&(rnumber<paDNA+pdaDNA+paMdm2+pdaMdm2)                                Amdm2=Amdm2-1; end; %Mdm2 deactivation
if (rnumber>=paDNA+pdaDNA+paMdm2+pdaMdm2)&(rnumber<paDNA+pdaDNA+paMdm2+pdaMdm2+paPTEN)                 Apten=Apten+1; end; %PTEN activation
if (rnumber>=paDNA+pdaDNA+paMdm2+pdaMdm2+paPTEN)&(rnumber<paDNA+pdaDNA+paMdm2+pdaMdm2+paPTEN+pdaPTEN)  Apten=Apten-1; end; %PTEN deactivation
