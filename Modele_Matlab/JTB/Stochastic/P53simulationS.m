%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Stochastic simulations of P53|MDM2 pathway          %
%   Main program                                        %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

clear all
clc

tic

N=1;                  % Number of cells considered
DNASw=1;              % DNA repair ON (1) or OFF (0)
ExtSw=1;              % Extended (1) or normal model (0)

Gy=0;                % Radiation dose in Gy (critical 1.88349)

NAmdm2=2;             % Number of Mdm2  alleles
NApten=2;             % Number of  PTEN alleles

tp1=60*3600;          % length of waiting for equilibrium phase (hours*3600s)
tp2=60*60;            % length of radiation phase (minutes*60s)
tp3=48*3600;          % length of after radiation phase (hours*3600s)

te=5*60;              % 5min Expected time of gene activity
DNAGy=35;             % 35 Expected number of DNA breaks per Gy
Tbreaks=DNAGy*Gy/tp2; % DNA damage coefficient

kk=10;                %makes time step shorter in radiation phase

tupeq=6*3600;         % time up to which the switching time distribution is derived while waiting for equillibrium
tupexp=4*3600;        % time up to which the switching time distribution is derived while IR is on

Yav=0;                % Average outputs

NBAV=0;               % DNA damage
Amdm2AV=0;
AptenAV=0;

for i=1:N

    %########## initial conditions #############
    i
    y0=zeros(1,11);
    
    if NApten==0 % without PTEN
    
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
        y0(11)=0;          % Apoptotic factors

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
        y0(11)=0;          % Apoptotic factors

    end 
    
    NB=0;
    NBC=[];
    Amdm2=0;
    Amdm2C=[];
    Apten=0;
    AptenC=[];
    
    yy0=y0; % to save oryginal y0 value

 %#########################################################################
 % First phase: before IR
 
    dt=10;                  % simulation step s
    realtime=0;
    IR=0;                   % IR off
    tspan1=[0:dt:tupeq];
    Y=yy0;
    T=zeros(1,1);
    
    NBC=[NB];
    Amdm2C=[Amdm2];
    AptenC=[Apten];
    
    while (realtime<tp1) 
        [T0,Y0]=ode23tb(@P53modelS,tspan1,yy0,[],NB,Amdm2,Apten,te,DNASw,ExtSw);
    
        p53pX=Y0(:,8);
        AFX=Y0(:,11);
        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        
        [Tchange,NB,Amdm2,Apten]=P53statuschange(IR,te,p53pX,NAmdm2,NApten,NBX,Tbreaks,Amdm2X,AptenX,dt,DNASw,AFX,ExtSw);
        
        tc=T0(Tchange);
        yy0=Y0(Tchange,:);
    
        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
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
    end;

    clear p53pX Y0 T0 Tchange tc;
    
    %######################################################################
    % Second phase IR on
    
    dt=10/kk;                % simulation step s
    realtime=tp1;
    IR=1;                    % IR on
    tspan1=[0:dt:tupexp/kk]; %Because we want correct numbers of points dt=dt/kk tupexp=tupexp/kk
  
    while (realtime<tp1+tp2) 
        [T0,Y0]=ode23tb(@P53modelS,tspan1,yy0,[],NB,Amdm2,Apten,te,DNASw,ExtSw);
    
        p53pX=Y0(:,8);
        AFX=Y0(:,11);
        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        
        [Tchange,NB,Amdm2,Apten]=P53statuschange(IR,te,p53pX,NAmdm2,NApten,NBX,Tbreaks,Amdm2X,AptenX,dt,DNASw,AFX,ExtSw);
        
        tc=T0(Tchange);
        yy0=Y0(Tchange,:);
    
        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NBX*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
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
    end;

    clear p53pX Y0 T0 Tchange tc;
    
    %#####################################################################
    % third phase IR off
    
    dt=10;                 % simulation step [s]
    tspan1=[0:dt:tupexp];
    realtime=tp1+tp2;
    IR=0;                  % IR off
        
    while (realtime<tp1+tp2+tp3) 
        [T0,Y0]=ode23tb(@P53modelS,tspan1,yy0,[],NB,Amdm2,Apten,te,DNASw,ExtSw);
    
        p53pX=Y0(:,8);
        AFX=Y0(:,11);
        NBX=NB;
        Amdm2X=Amdm2;
        AptenX=Apten;
        
        [Tchange,NB,Amdm2,Apten]=P53statuschange(IR,te,p53pX,NAmdm2,NApten,NBX,Tbreaks,Amdm2X,AptenX,dt,DNASw,AFX,ExtSw);
        
        tc=T0(Tchange);
        yy0=Y0(Tchange,:);
    
        Y=[Y;Y0(2:Tchange,:)]; 
        T=[T;T0(2:Tchange)+realtime];
        NBC=[NBC;NB*ones(Tchange-1,1)];
        Amdm2C=[Amdm2C;Amdm2X*ones(Tchange-1,1)];
        AptenC=[AptenC;AptenX*ones(Tchange-1,1)];
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
    end;
    
    clear p53pX Y0 T0 Tchange tc;
    
    %##########################################################################

    TScatter=find(T<(tp1+48*3600),1,'last');

    Scatter(i,1)=Y(TScatter,6); %MDM2pn
    Scatter(i,2)=Y(TScatter,8); %P53pn

    Yav=Yav+Y;
    NBAV=NBAV+NBC;
    Amdm2AV=Amdm2AV+Amdm2C;
    AptenAV=AptenAV+AptenC;

end

Yav=Yav/N;
NBAV=NBAV/N;
Amdm2AV=Amdm2AV/N;
AptenAV=AptenAV/N;

T=T/3600;

toc

P53plotsS;
