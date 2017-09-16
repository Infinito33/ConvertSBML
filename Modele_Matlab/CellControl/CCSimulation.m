%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.03.2015                     %
%   CC Model simulation                                 %
%   Main program                                        %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc
starttime=clock;      % curent time

    %%%%%%% simulations phases setup 
    
    %1 phase setup
    tp1=20*3600;        % length of waiting for equilibrium phase (hours*3600s)
    Gy1=0;
	Inp1a=0;              % Nutlin concentration in uM Zhang Case
    Inp2a=0;              % Nutlin concentration switch
    Inp3a=0;              % Nutlin concentration in M Vasillev case
    RNAiMdm2a=0;          % in nM 30 - min guarantee 75%, 100-300 from articles. 20 000 max reported.
    RNAiPTENa=0;
    
    %2 phase setup
    tp2=5*60;%1*3600;       % length of radiation phase - it have to be 1h ! (hours*3600s)
    Gy2=0;            % IR dose in Gy, critical is 2.019
	Inp1b=0;              % Nutlin concentration in uM Zhang Case
    Inp2b=0;              % Nutlin concentration switch
    Inp3b=0;              % Nutlin concentration in M Vasillev case
    RNAiMdm2b=0; % in nM 30 - min guarantee 75%, 100-300 from articles. 20 000 max reported.
    RNAiPTENb=0;
    
    %3 phase setup
    tp3=4*3600;%1*3600;       % length of radiation phase - it have to be 1h ! (hours*3600s)
    Gy3=0;            % IR dose in Gy, critical is 1.97431706891
	Inp1c=0;              % Nutlin concentration in uM Zhang Case
    Inp2c=0;              % Nutlin concentration switch
    Inp3c=0;              % Nutlin concentration in M Vasillev case
    RNAiMdm2c=0; % in nM 30 - min guarantee 75%, 100-300 from articles. 20 000 max reported.
    RNAiPTENc=0;
    
    %4 phase setup
    tp4=48*3600;       % length of Mdm2 RNAi phase (hours*3600s)
    Gy4=0;             %   
	Inp1d=0;              % Nutlin concentration in uM Zhang Case
    Inp2d=0;              % Nutlin concentration switch
    Inp3d=0;              % Nutlin concentration in M Vasillev case
    RNAiMdm2d=0; % in nM 30 - min guarantee 75%, 100-300 from articles. 20 000 max reported.
    RNAiPTENd=0;

%########## initial conditions #############

y0=zeros(1,32);

y0(1)=188300; %APIP3
y0(2)=1234; %AAKT
y0(3)=57990; %MDM2
y0(4)=16345; %MDM2P
y0(5)=81218; %PTEN
y0(6)=21228; %MDM2N
y0(7)=15976; %P53
y0(8)=17502; %P53P
y0(9)=245; %MDM2t
y0(10)=314; %PTENt
y0(11)=339; %P53t
y0(12)=0; %GMDM2
y0(13)=0; %GPTEN
y0(14)=0; %GP53
y0(15)=0; %IMDM2
y0(16)=0; %IMDM2P
y0(17)=0; %IMDM2N
y0(18)=0; %NUT
y0(19)=1353; %P53MDM2N
y0(20)=148; %P53PMDM2N
y0(21)=278; %P53UMDM2N
y0(22)=3; %P53PUMDM2N
y0(23)=3286; %P53U
y0(24)=400; %P53PU
y0(25)=451; %P53UU
y0(26)=6; %P53PUU
y0(27)=0; %NUTET
y0(28)=8585; %AF
y0(29)=0; %TIME
y0(30)=0; %DSB
y0(31)=0; %siRNA for Mdm2t
y0(32)=0; %siRNA for PTEN
       
yy0=y0; % to save oryginal y0 value

% Begining of the simulation part
%#########################################################################
% 1 phase: before IR and RNAi
 
dt=10;                  % simulation step s
realtime=0;  
    
tspan=[0:dt:tp1];
Y=yy0;
T=zeros(1,1);

[T0,Y0]=ode23tb(@CCModel,tspan,yy0,[],Gy1,Inp1a,Inp2a,Inp3a,RNAiMdm2a,RNAiPTENa);
    
yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];
    
%#########################################################################
% 2 phase: IR on, no RNAi

dt=0.01;                  % simulation step s (maybe 1)
realtime=tp1;
    
tspan=[0:dt:tp2];

[T0,Y0]=ode23tb(@CCModel,tspan,yy0,[],Gy2,Inp1b,Inp2b,Inp3b,RNAiMdm2b,RNAiPTENb);
    
yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];
%#########################################################################
% 2 phase: IR off, no RNAi on

dt=1;                  % simulation step s (maybe 1)
realtime=tp1+tp2;
    
tspan=[0:dt:tp3];

[T0,Y0]=ode23tb(@CCModel,tspan,yy0,[],Gy3,Inp1c,Inp2c,Inp3c,RNAiMdm2c,RNAiPTENc);
    
yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% 4 phase: no IR, no RNAi

dt=10;                  % simulation step s (maybe 1)
realtime=tp1+tp2+tp3;                 
    
tspan=[0:dt:tp4];

[T0,Y0]=ode23tb(@CCModel,tspan,yy0,[],Gy4,Inp1d,Inp2d,Inp3d,RNAiMdm2d,RNAiPTENd);

yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% end of simulation part

T=T/3600;

simulation_time=etime(clock,starttime)/3600 %simulation time in hours

CCPlots %plots