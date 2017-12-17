
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Deterministic simulations of P53|MDM2 pathway       %
%   Main program                                        %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc

Tswitch=0; % Show (0) or don't show (1) equilibrium phase on plots

PTENSw=0;             % PTEN mRNA production ON (1) or OFF (0)
DNASw=0;              % DNA repair ON (1) or OFF (0)
ExtSw=0;              % Extended (1) or normal model (0)

Gy=0;                % Radiation dose in Gy (critical 1.88349)

tp1=60*3600;          % length of waiting for equilibrium phase (hours*3600s)
tp2=60*60;            % length of radiation phase (minutes*60s)
tp3=59*3600;          % length of after radiation phase (hours*3600s)

te=5*60;              % 5min Expected time of gene activity
DNAGy=35;             % 35 Expected number of DNA breaks per Gy
Tbreaks=DNAGy*Gy/tp2; % DNA damage coefficient

dt=10;   % simulation step s

Yav=0;          % Average outputs
    
    y0=zeros(1,12);

if PTENSw==0 % without PTEN
    
    y0(1)=100000;      % PIP3a active form of PIP3 (PIP3a)                           
    y0(2)=100000;      % AKTa active form of AKT (AKTa)                
    y0(3)=8647;        % MDM2 cytoplasmic (MDM2)             
    y0(4)=1.373*10^4;  % phospho-MDM2 cytoplasmic (MDM2p)                                    
    y0(5)=0;           % PTEN cytoplasmic (PTEN)                             
    y0(6)=2.288*10^5;  % phospho-MDM2 nuclear (MDM2pn)                       
    y0(7)=3.681*10^4;  % P53 nuclear (P53n)                   
    y0(8)=5905;        % phospho-P53 nuclear (P53pn)                   
    y0(9)=15;          % MDM2 transcript (MDM2t)                       
    y0(10)=0;          % PTEN transcript (PTENt)                  
    y0(11)=0;          % DNA damage level (DNAdam)  
    y0(12)=0;          % Apoptotic factors

else % with PTEN

    y0(1)=3.957*10^4;  % PIP3a active form of PIP3 (PIP3a)                           
    y0(2)=5.67*10^4;   % AKTa active form of AKT (AKTa)                
    y0(3)=1.506*10^4;  % MDM2 cytoplasmic (MDM2)             
    y0(4)=1.355*10^4;  % phospho-MDM2 cytoplasmic (MDM2p)                                    
    y0(5)=3.054*10^4;  % PTEN cytoplasmic (PTEN)                             
    y0(6)=2.259*10^5;  % phospho-MDM2 nuclear (MDM2pn)                       
    y0(7)=3.77*10^4;   % P53 nuclear (P53n)                   
    y0(8)=6.178*10^3;  % phospho-P53 nuclear (P53pn)                   
    y0(9)=15.27;       % MDM2 transcript (MDM2t)                       
    y0(10)=15.27;      % PTEN transcript (PTENt)                  
    y0(11)=0;          % DNA damage level (DNAdam)  
    y0(12)=0;          % Apoptotic factors
    
end

    yy0=y0; % to save oryginal y0 value

% ####################################################
% First phase: before IR
    
    realtime=0;
    Y=yy0;
    T=zeros(1,1);
    IR=0;
    tspan=[0:dt:tp1];
               
    [T0,Y0]=ode23tb(@P53modelD,tspan,yy0,[],IR,PTENSw,DNASw,te,Tbreaks,ExtSw);

    yy0=Y0(size(Y0,1),:);     
    T=[T;T0+realtime];
    Y=[Y;Y0];
         
% ##################################3
% Second phase IR on

    realtime=tp1;
    IR=1;
    tspan=[0:dt:tp2];
               
    [T0,Y0]=ode23tb(@P53modelD,tspan,yy0,[],IR,PTENSw,DNASw,te,Tbreaks,ExtSw);

    yy0=Y0(size(Y0,1),:);     
    T=[T;T0+realtime];
    Y=[Y;Y0];
    
%##################################
% Third phase IR off
    
    realtime=tp1+tp2;
    IR=0;  
    tspan=[0:dt:tp3];
               
    [T0,Y0]=ode23tb(@P53modelD,tspan,yy0,[],IR,PTENSw,DNASw,te,Tbreaks, ExtSw);

    yy0=Y0(size(Y0,1),:);     
    T=[T;T0+realtime];
    Y=[Y;Y0];
    
%###################################

TScatter=find(T<(tp1+48*3600),1,'last');

ScatterD(1,1)=Y(TScatter,6); %MDM2pn
ScatterD(1,2)=Y(TScatter,8); %P53pn

Yav=Y;

if Tswitch==1
    Tminus=find(T==tp1,1)-3600;
    Tend=size(T,1);
    T=T(Tminus:Tend,:);
    T=T-tp1;
    Yav=Yav(Tminus:Tend,:);
end

T=T/3600;

P53plotsD;
