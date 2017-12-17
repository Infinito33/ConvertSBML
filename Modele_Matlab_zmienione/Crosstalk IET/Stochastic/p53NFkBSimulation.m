%#################################################
%                                                #
% p53-NFkB stochastic simulations - main program #
%                                                #
% KP 02.03.2009                                  #
%                                                #
%#################################################

clear all
clc
starttime=clock; % curent time

name='name.mat';    % name of the file to save results

N=1;

TNF=10;          % TNF dose, doses: Allan=20, Lee=10, Hoffmann=10 or 1,
Gy=2;

% Critical values  
% Gy=1.993 (No TNF, protocol 2) 
% Gy=2.240 (TNF before IR, protocol 0)
% Gy=1.249 (TNF after IR, protocol 1)

% see details of protocols
ProtocolSwitch=0; %TNF before 0, after 1, noTNF 2

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
tp0=1*3600;           % length of the radiation phase

DNASw=1;              % DNA repair ON (1) or OFF (0)
ExtSw=1;              % Switch on (1) or off (0) action of apoptotic factors

dta=5*60/3600;        % time between next points in saved results minutes*60/3600

tupeq=2*3600;         % 25*3600 time up to which the switching time distribution is derived while waiting for equillibrium
tupexp=2*3600;        % 10*3600 time up to which the switching time distribution is derived while IR or TNF is on

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

YAV=0;                 % Average outputs
NBAV=0;                % DNA damage
Amdm2AV=0;             % average status(t) of Mdm2 gene
AptenAV=0;             % average status(t) of PTEN gene
Ap53AV=0;              % average status(t) of p53 gene
AikbaAV=0;             % average status(t) of IkBa gene
Aa20AV=0;              % average status(t) of A20 gene 
BAV=0;                 % average number of active receptors

for ttti=1:round(sum(clock))     % randomization of randomization
    rand;
end;

for i=1:N
i
%########## initial conditions #############

    [Th,a0,a1,a1n,a2,a2n,a3,a3n,a4,a5,a6,c0,c1,c1n,c2,c3,c3n,c4n,c5a,c5n,c6a,d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,e0,e1a,e2a,h0,h1,i0,i1,i1a,k1,k2,k3,k4,ka20,kv,n0,n1,p1,s0,s1,s2,s3,t0,t1,t2,q0,q1,q1n,q2,q2n,q3,q4,q5,q6,drep,NSAT,tp,KN,KNN,ka,AB,kAB,ki,kb,kf,AKTtot,PIPtot,M,Tbreaks,NAmdm2,NApten,NAp53,NAa20,NAikba,kk]=p53NFkBParam(DNASw,ExtSw,Gy,tp0);
    y0=zeros(1,31);

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

    yy0=y0; % to save oryginal y0 value

    NB=0;
    Amdm2=0;
    Apten=0;
    Ap53=0;
    Aikba=0;
    Aa20=0;
    B=0;

    NBC=[NB];
    Amdm2C=[Amdm2];
    AptenC=[Apten];
    Ap53C=[Ap53];
    AikbaC=[Aikba];
    Aa20C=[Aa20];
    BC=[B];

    % Begining of the simulation part
    %#########################################################################
    % 1 phase: before IR and TNF

    dt=10;                  % simulation step s
    realtime=0;

    IR=tp1ir;                   
    TR=tp1tnf*TNF;

    tspan=[0:dt:tupeq];
    Y=yy0;
    T=zeros(1,1);

    while (realtime<tp1)

        [T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,Amdm2,Apten,Ap53,Aikba,Aa20,NB,B,Gy,tp0);

        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        Ap53X=Ap53;
        AikbaX=Aikba;
        Aa20X=Aa20;
        BX=B;
        Lp53=Y0(:,10);
        LNFkB=Y0(:,23);
        LIkBa=Y0(:,27);
        LAF=Y0(:,13);

        [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

        tc=T0(Tchange);
        yy0=Y0(Tchange,:);

        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
        Ap53C=[Ap53C;Ap53X*ones(Tchange-1,1)];
        AikbaC=[AikbaC;AikbaX*ones(Tchange-1,1)];
        Aa20C=[Aa20C;Aa20X*ones(Tchange-1,1)];
        BC=[BC;BX*ones(Tchange-1,1)];
        realtime=realtime+tc;
    end

    nn=0;

    if (realtime>tp1)

        nn=(realtime-tp1)/dt;
        x=size(Y);
        Y=Y(1:(x(1)-nn),:);
        T=T(1:(x(1)-nn));
        yy0=Y0(Tchange-nn,:);

        x1=length(NBC);
        NBC=NBC(1:x1-nn);
        NB=NBX;
        Amdm2C=Amdm2C(1:x1-nn);
        Amdm2=Amdm2X;
        AptenC=AptenC(1:x1-nn);
        Apten=AptenX;
        Ap53C=Ap53C(1:x1-nn);
        Ap53=Ap53X;
        AikbaC=AikbaC(1:x1-nn);
        Aikba=AikbaX;
        Aa20C=Aa20C(1:x1-nn);
        Aa20=Aa20X;
        BC=BC(1:x1-nn);
        B=BX;
    end;

    clear Lp53 LNFkB LIkBa LAF Y0 T0 Tchange tc;

    %#########################################################################
    % 2 phase: IR or TNF on

    dt=10/kk;                  % simulation step s (maybe 1)
    realtime=tp1;

    IR=tp2ir;                    
    TR=tp2tnf*TNF;

    tspan=[0:dt:tupexp/kk];

    while (realtime<tp1+tp2)

        [T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,Amdm2,Apten,Ap53,Aikba,Aa20,NB,B,Gy,tp0);

        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        Ap53X=Ap53;
        AikbaX=Aikba;
        Aa20X=Aa20;
        BX=B;
        Lp53=Y0(:,10);
        LNFkB=Y0(:,23);
        LIkBa=Y0(:,27);
        LAF=Y0(:,13);

        [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

        tc=T0(Tchange);
        yy0=Y0(Tchange,:);

        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
        Ap53C=[Ap53C;Ap53X*ones(Tchange-1,1)];
        AikbaC=[AikbaC;AikbaX*ones(Tchange-1,1)];
        Aa20C=[Aa20C;Aa20X*ones(Tchange-1,1)];
        BC=[BC;BX*ones(Tchange-1,1)];
        realtime=realtime+tc;
    end

    nn=0;

    if (realtime>tp1+tp2)

        nn=(realtime-tp1-tp2)/dt;
        x=size(Y);
        Y=Y(1:(x(1)-nn),:);
        T=T(1:(x(1)-nn));
        yy0=Y0(Tchange-nn,:);

        x1=length(NBC);
        NBC=NBC(1:x1-nn);
        NB=NBX;
        Amdm2C=Amdm2C(1:x1-nn);
        Amdm2=Amdm2X;
        AptenC=AptenC(1:x1-nn);
        Apten=AptenX;
        Ap53C=Ap53C(1:x1-nn);
        Ap53=Ap53X;
        AikbaC=AikbaC(1:x1-nn);
        Aikba=AikbaX;
        Aa20C=Aa20C(1:x1-nn);
        Aa20=Aa20X;
        BC=BC(1:x1-nn);
        B=BX;
    end;

    clear Lp53 LNFkB LIkBa LAF Y0 T0 Tchange tc;

    %#########################################################################
    % 3 phase: no IR and TNF

    dt=10/kk;                  % simulation step s (maybe 1)
    realtime=tp1+tp2;

    IR=tp3ir;                    
    TR=tp3tnf*TNF;

    tspan=[0:dt:tupexp/kk];

    while (realtime<tp1+tp2+tp3)

        [T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,Amdm2,Apten,Ap53,Aikba,Aa20,NB,B,Gy,tp0);

        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        Ap53X=Ap53;
        AikbaX=Aikba;
        Aa20X=Aa20;
        BX=B;
        Lp53=Y0(:,10);
        LNFkB=Y0(:,23);
        LIkBa=Y0(:,27);
        LAF=Y0(:,13);

        [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

        tc=T0(Tchange);
        yy0=Y0(Tchange,:);

        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
        Ap53C=[Ap53C;Ap53X*ones(Tchange-1,1)];
        AikbaC=[AikbaC;AikbaX*ones(Tchange-1,1)];
        Aa20C=[Aa20C;Aa20X*ones(Tchange-1,1)];
        BC=[BC;BX*ones(Tchange-1,1)];
        realtime=realtime+tc;
    end

    nn=0;

    if (realtime>tp1+tp2+tp3)

        nn=(realtime-tp1-tp2-tp3)/dt;
        x=size(Y);
        Y=Y(1:(x(1)-nn),:);
        T=T(1:(x(1)-nn));
        yy0=Y0(Tchange-nn,:);

        x1=length(NBC);
        NBC=NBC(1:x1-nn);
        NB=NBX;
        Amdm2C=Amdm2C(1:x1-nn);
        Amdm2=Amdm2X;
        AptenC=AptenC(1:x1-nn);
        Apten=AptenX;
        Ap53C=Ap53C(1:x1-nn);
        Ap53=Ap53X;
        AikbaC=AikbaC(1:x1-nn);
        Aikba=AikbaX;
        Aa20C=Aa20C(1:x1-nn);
        Aa20=Aa20X;
        BC=BC(1:x1-nn);
        B=BX;
    end;

    clear Lp53 LNFkB LIkBa LAF Y0 T0 Tchange tc;

    %#########################################################################
    % 4 phase: IR or TNF on

    dt=10/kk;                  % simulation step s (maybe 1)
    realtime=tp1+tp2+tp3;

    IR=tp4ir;                    
    TR=tp4tnf*TNF;

    tspan=[0:dt:tupexp/kk];

    while (realtime<tp1+tp2+tp3+tp4)

        [T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,Amdm2,Apten,Ap53,Aikba,Aa20,NB,B,Gy,tp0);

        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        Ap53X=Ap53;
        AikbaX=Aikba;
        Aa20X=Aa20;
        BX=B;
        Lp53=Y0(:,10);
        LNFkB=Y0(:,23);
        LIkBa=Y0(:,27);
        LAF=Y0(:,13);

        [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

        tc=T0(Tchange);
        yy0=Y0(Tchange,:);

        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
        Ap53C=[Ap53C;Ap53X*ones(Tchange-1,1)];
        AikbaC=[AikbaC;AikbaX*ones(Tchange-1,1)];
        Aa20C=[Aa20C;Aa20X*ones(Tchange-1,1)];
        BC=[BC;BX*ones(Tchange-1,1)];
        realtime=realtime+tc;
    end

    nn=0;

    if (realtime>tp1+tp2+tp3+tp4)

        nn=(realtime-tp1-tp2-tp3-tp4)/dt;
        x=size(Y);
        Y=Y(1:(x(1)-nn),:);
        T=T(1:(x(1)-nn));
        yy0=Y0(Tchange-nn,:);

        x1=length(NBC);
        NBC=NBC(1:x1-nn);
        NB=NBX;
        Amdm2C=Amdm2C(1:x1-nn);
        Amdm2=Amdm2X;
        AptenC=AptenC(1:x1-nn);
        Apten=AptenX;
        Ap53C=Ap53C(1:x1-nn);
        Ap53=Ap53X;
        AikbaC=AikbaC(1:x1-nn);
        Aikba=AikbaX;
        Aa20C=Aa20C(1:x1-nn);
        Aa20=Aa20X;
        BC=BC(1:x1-nn);
        B=BX;
    end;

    clear Lp53 LNFkB LIkBa LAF Y0 T0 Tchange tc;

    %#########################################################################
    % 5: no IR or TNF

    dt=10;                  % simulation step s (maybe 1)
    realtime=tp1+tp2+tp3+tp4;

    IR=tp5ir;                    
    TR=tp5tnf*TNF;

    tspan=[0:dt:tupexp];

    while (realtime<tp1+tp2+tp3+tp4+tp5)

        [T0,Y0]=ode23tb(@p53NFkBModel,tspan,yy0,[],DNASw,ExtSw,Amdm2,Apten,Ap53,Aikba,Aa20,NB,B,Gy,tp0);

        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        Ap53X=Ap53;
        AikbaX=Aikba;
        Aa20X=Aa20;
        BX=B;
        Lp53=Y0(:,10);
        LNFkB=Y0(:,23);
        LIkBa=Y0(:,27);
        LAF=Y0(:,13);

        [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

        tc=T0(Tchange);
        yy0=Y0(Tchange,:);

        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
        Ap53C=[Ap53C;Ap53X*ones(Tchange-1,1)];
        AikbaC=[AikbaC;AikbaX*ones(Tchange-1,1)];
        Aa20C=[Aa20C;Aa20X*ones(Tchange-1,1)];
        BC=[BC;BX*ones(Tchange-1,1)];
        realtime=realtime+tc;
    end

    nn=0;

    if (realtime>tp1+tp2+tp3+tp4+tp5)

        nn=(realtime-tp1-tp2-tp3-tp4-tp5)/dt;
        x=size(Y);
        Y=Y(1:(x(1)-nn),:);
        T=T(1:(x(1)-nn));
        yy0=Y0(Tchange-nn,:);

        x1=length(NBC);
        NBC=NBC(1:x1-nn);
        NB=NBX;
        Amdm2C=Amdm2C(1:x1-nn);
        Amdm2=Amdm2X;
        AptenC=AptenC(1:x1-nn);
        Apten=AptenX;
        Ap53C=Ap53C(1:x1-nn);
        Ap53=Ap53X;
        AikbaC=AikbaC(1:x1-nn);
        Aikba=AikbaX;
        Aa20C=Aa20C(1:x1-nn);
        Aa20=Aa20X;
        BC=BC(1:x1-nn);
        B=BX;
    end;

    clear Lp53 LNFkB LIkBa LAF Y0 T0 Tchange tc;

    %#########################################################################
    % end of simulation part

    TScatter=find(T<(tp1+tp2+tp3+tp4+tp5),1,'last');

    Scatter(i,1)=Y(TScatter,4);     %AKTp
    Scatter(i,2)=Y(TScatter,10);    %P53pn

    YAV=YAV+Y;                  % Average outputs
    NBAV=NBAV+NBC;              % DNA damage
    Amdm2AV=Amdm2AV+Amdm2C;     % average status(t) of Mdm2 gene
    AptenAV=AptenAV+AptenC;     % average status(t) of PTEN gene
    Ap53AV=Ap53AV+Ap53C;        % average status(t) of p53 gene
    AikbaAV=AikbaAV+AikbaC;     % average status(t) of IkBa gene
    Aa20AV=Aa20AV+Aa20C;        % average status(t) of A20 gene 
    BAV=BAV+BC;                 % average number of active receptors

%####################################################################
% Results saving

    dtt=0;
    Tindex=[];
    Tt=T/3600; 
    
    while dtt<=max(Tt)
        Tindex=[Tindex find(Tt>dtt,1)];
        dtt=dtt+dta;
    end
    
    Tt=Tt(Tindex);
    
    ResultsY(i,:,:)=Y(Tindex,:);
    ResultsDiscrete(i,:,1)=NBC(Tindex);
    ResultsDiscrete(i,:,2)=Amdm2C(Tindex);
    ResultsDiscrete(i,:,3)=AptenC(Tindex);
    ResultsDiscrete(i,:,4)=Ap53C(Tindex);
    ResultsDiscrete(i,:,5)=AikbaC(Tindex);
    ResultsDiscrete(i,:,6)=Aa20C(Tindex);
    ResultsDiscrete(i,:,7)=BC(Tindex);
    	
    save(name,'i','TNF','Gy','Tt','ResultsY','ResultsDiscrete','Scatter','ProtocolSwitch','DNASw','ExtSw')

%#####################################################
% End of the results saving
    
end

YAV=YAV/N;                 % average outputs
NBAV=NBAV/N;               % DNA damage
Amdm2AV=Amdm2AV/N;         % average status(t) of Mdm2 gene
AptenAV=AptenAV/N;         % average status(t) of PTEN gene
Ap53AV=Ap53AV/N;           % average status(t) of p53 gene
AikbaAV=AikbaAV/N;         % average status(t) of IkBa gene
Aa20AV=Aa20AV/N;           % average status(t) of A20 gene 
BAV=BAV/N;                 % average number of active receptors

T=T/3600;

if (N==1)
    NBAV=NBAV+0.001;             % for nice pictures 
    Amdm2AV=Amdm2AV+0.001;                
    AptenAV=AptenAV+0.001;
    Ap53AV=Ap53AV+0.001;
    AikbaAV=AikbaAV+0.001;
    Aa20AV=Aa20AV+0.001;
    BAV=BAV+0.001;

end;

simulation_time=etime(clock,starttime)/3600 % simulation time in hours

p53NFkBPlots % plots