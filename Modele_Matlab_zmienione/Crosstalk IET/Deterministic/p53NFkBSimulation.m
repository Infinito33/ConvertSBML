%####################################################
%                                                   #
% p53-NFkB deterministic simulations - main program #
%                                                   #
% KP 02.03.2009                                     #
%                                                   #
%####################################################

clear all
clc
starttime=clock;    % curent time

name='name.mat';       % name of the file to save results

TNF=0;             %10 TNF dose, doses: Allan=20, Lee=10, Hoffmann=10 or 1, 
Gy=2;

% Critical values  
% Gy=1.993 (No TNF, protocol 2) 
% Gy=2.240 (TNF before IR, protocol 0)
% Gy=1.249 (TNF after IR, protocol 1)

% see details of protocols
ProtocolSwitch=2; %TNF before 0, after 1, noTNF 2

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tp0=1*3600;           % length of the radiation phase

DNASw=1;              % DNA repair ON (1) or OFF (0)
ExtSw=1;              % Switch on (1) or off (0) action of apoptotic factors

%#####################################################################

if ProtocolSwitch==0

%%%%%%  TNF before %%%%%% total simulation time 67 h simulation continues
%%%%%%  48 hours ofter the protocol

    %1 phase setup
    tp1=15*3600;        % length of waiting for equilibrium phase (hours*3600s)
    tp1ir=0;            % IR on (1) or off (0)    
    tp1tnf=0;           % TNF on (1) or off (0)
    %2 phase setup
    tp2=3*3600;         % length of second phase (minutes*60s)
    tp2ir=0;            % IR on (1) or off (0)    
    tp2tnf=1;           % TNF on (1) or off (0)
    %3 phase setup
    tp3=1*3600;         % length of third phase (hours*3600s)
    tp3ir=1;            % IR on (1) or off (0)    
    tp3tnf=1;           % TNF on (1) or off (0)
    %4 phase setup
    tp4=1*3600;         % length of fourth phase (hours*3600s)
    tp4ir=0;            % IR on (1) or off (0)    
    tp4tnf=0;           % TNF on (1) or off (0)
    %5 phase setup
    tp5=47*3600;        % length of the last phase (hours*3600s)
    tp5ir=0;            % IR on (1) or off (0)    
    tp5tnf=0;           % TNF on (1) or off (0)

elseif ProtocolSwitch==1

%%%%%%% TNF after %%%%%%%%%%% total simulation time 76 h, simulation
%%%%%%% continues 48 h after the protocol

    %1 phase setup
    tp1=15*3600;        % length of waiting for equilibrium phase (hours*3600s)
    tp1ir=0;            % IR on (1) or off (0)    
    tp1tnf=0;           % TNF on (1) or off (0)
    %2 phase setup
    tp2=1*3600;         % length of second phase (minutes*60s)
    tp2ir=1;            % IR on (1) or off (0)    
    tp2tnf=0;           % TNF on (1) or off (0)
    %3 phase setup
    tp3=8*3600;         % length of third phase (hours*3600s)
    tp3ir=0;            % IR on (1) or off (0)    
    tp3tnf=0;           % TNF on (1) or off (0)
    %4 phase setup
    tp4=4*3600;         % length of fourth phase (hours*3600s)
    tp4ir=0;            % IR on (1) or off (0)    
    tp4tnf=1;           % TNF on (1) or off (0)
    %5 phase setup
    tp5=48*3600;        % length of the last phase (hours*3600s)
    tp5ir=0;            % IR on (1) or off (0)    
    tp5tnf=0;           % TNF on (1) or off (0)

else

    %%%%%%% no TNF %%%%%%%%%%% total simulation 64 h, simulation continues
    %%%%%%% 48 h after protocol

    %1 phase setup
    tp1=15*3600;        % length of waiting for equilibrium phase (hours*3600s)
    tp1ir=0;            % IR on (1) or off (0)    
    tp1tnf=0;           % TNF on (1) or off (0)
    %2 phase setup
    tp2=1*3600;         % length of second phase (minutes*60s)
    tp2ir=1;            % IR on (1) or off (0)    
    tp2tnf=0;           % TNF on (1) or off (0)
    %3 phase setup
    tp3=8*3600;         % length of third phase (hours*3600s)
    tp3ir=0;            % IR on (1) or off (0)    
    tp3tnf=0;           % TNF on (1) or off (0)
    %4 phase setup
    tp4=4*3600;         % length of fourth phase (hours*3600s)
    tp4ir=0;            % IR on (1) or off (0)    
    tp4tnf=0;           % TNF on (1) or off (0)
    %5 phase setup
    tp5=36*3600;        % length of the last phase (hours*3600s)
    tp5ir=0;            % IR on (1) or off (0)    
    tp5tnf=0;           % TNF on (1) or off (0)
    
end

%########## initial conditions #############

[Th,a0,a1,a1n,a2,a2n,a3,a3n,a4,a5,a6,c0,c1,c1n,c2,c3,c3n,c4n,c5a,c5n,c6a,d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,e0,e1a,e2a,h0,h1,i0,i1,i1a,k1,k2,k3,k4,ka20,kv,n0,n1,p1,s0,s1,s2,s3,t0,t1,t2,q0,q1,q1n,q2,q2n,q3,q4,q5,q6,drep,NSAT,tp,KN,KNN,ka,AB,kAB,ki,kb,kf,AKTtot,PIPtot,M,Tbreaks,NAmdm2,NApten,NAp53,NAa20,NAikba]=p53NFkBParam(DNASw,ExtSw,Gy,tp0);

y0=zeros(1,38);

y0(1)=6.1608e+004;
y0(2)=3.8392e+004;
y0(3)=1.4452e+005;
y0(4)=5.5483e+004;
y0(5)=2.1110e+004;
y0(6)=1.3944e+004;
y0(7)=3.2094e+004;
y0(8)=2.3239e+005;
y0(9)=4.5603e+004;
y0(10)=7.1248e+003;
y0(11)=16.0468;
y0(12)=16.0468;
y0(13)=1.2674e+003;
y0(14)=KN;
y0(16)=KNN;
y0(22)=153.8276;
y0(23)=301.1542;
y0(24)=5.1560e+003*AB;
y0(25)=5.1560*AB;
y0(26)=5.8771e+003;
y0(27)=2.0432e+003;
y0(28)=5.1560;
y0(29)=9.9515e+004;
y0(30)=30.7655;
y0(31)=510.8155;
y0(34)=0.0802;
y0(35)=0.0802;
y0(36)=0.0433;
y0(37)=0.0433;
y0(38)=0.0433;
       
yy0=y0; % to save oryginal y0 value

% Begining of the simulation part
%#########################################################################
% 1 phase: before IR and TNF
 
dt=10;                  % simulation step s
realtime=0;
    
IR=tp1ir;                   
TR=tp1tnf*TNF;
    
tspan=[0:dt:tp1];
Y=yy0;
T=zeros(1,1);

[T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,TR,IR,Gy,tp0);
    
yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];
   
%#########################################################################
% 2 phase: IR or TNF on

dt=10;                  % simulation step s (maybe 1)
realtime=tp1;
    
IR=tp2ir;                    
TR=tp2tnf*TNF;
    
tspan=[0:dt:tp2];

[T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,TR,IR,Gy,tp0);
    
yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% 3 phase: no IR and TNF

dt=10;                  % simulation step s (maybe 1)
realtime=tp1+tp2;
    
IR=tp3ir;                   
TR=tp3tnf*TNF; 
    
tspan=[0:dt:tp3];

[T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,TR,IR,Gy,tp0);

yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% 4 phase: IR or TNF on

dt=10;                      % simulation step s (maybe 1)
realtime=tp1+tp2+tp3;
    
IR=tp4ir;                   % IR off
TR=tp4tnf*TNF;
    
tspan=[0:dt:tp4];

[T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,TR,IR,Gy,tp0);

yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% 5: no IR or TNF

dt=10;                       % simulation step s (maybe 1)
realtime=tp1+tp2+tp3+tp4;
    
IR=tp5ir;                    % IR off
TR=tp5tnf*TNF;
    
tspan=[0:dt:tp5];

[T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,TR,IR,Gy,tp0);

yy0=Y0(size(Y0,1),:);     
T=[T;T0+realtime];
Y=[Y;Y0];

%#########################################################################
% end of simulation part

T=T/3600;

save(name,'TNF','Gy','T','Y','ProtocolSwitch','DNASw','ExtSw')

simulation_time=etime(clock,starttime)/3600     % simulation time in hours

p53NFkBPlots     % plots