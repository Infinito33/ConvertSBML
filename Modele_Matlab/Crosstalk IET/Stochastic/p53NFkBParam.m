%###############################################
%                                              #
% p53-NFkB stochastic simulations - parameters #
%                                              #
% KP 02.03.2009                                #
%                                              #
%###############################################

function[Th,a0,a1,a1n,a2,a2n,a3,a3n,a4,a5,a6,c0,c1,c1n,c2,c3,c3n,c4n,c5a,c5n,c6a,d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,e0,e1a,e2a,h0,h1,i0,i1,i1a,k1,k2,k3,k4,ka20,kv,n0,n1,p1,s0,s1,s2,s3,t0,t1,t2,q0,q1,q1n,q2,q2n,q3,q4,q5,q6,drep,NSAT,tp,KN,KNN,ka,AB,kAB,ki,kb,kf,AKTtot,PIPtot,M,Tbreaks,NAmdm2,NApten,NAp53,NAa20,NAikba,kk]=p53NFkBParam(DNASw,ExtSw,Gy,tp0)

%##############################################################
%#################### p53 #####################################
%##############################################################

    AKTtot=2*10^5;    % 2*10^5     total amount of AKT
    PIPtot=10^5;      % 10^5       total amount of PIP3
    PTENmax=4*10^5;   % 4*10^5     max amount of PTEN 

%ai - activation/phosphorylation rate

    a0=1*10^-4;              % 1*10^-4           nuclear P53 standard phosphorylation rate   
    a1=1*10^-3;              % 1*10^-3           nuclear P53 forced by DNAdam phosphorylation rate
    a2=5*10^-5;              % 5*10^-5           cytoplasmic PIP3 activation rate
    a3=2*10^-4/PIPtot;       % 2*10^-4/PIPtot    cytoplasmic AKT activation rate                       
    a4=1.5*10^-3/AKTtot;     % 1.5*10^-3/AKTtot  cytoplasmic MDM2 phosphorylation rate                                         
    a5=2*10^-14;             % 2*10^-14          DNA repair rate   (gives drep )                    
    a6=ExtSw*0.01;           % 0.01              maximum apoptotic DNA degradation                 
    
%ci - deactivation/dephosphorylation rate

    c0=1*10^-3/PTENmax;      % 1*10^-3/PTENmax   cytoplasmic PIP3 forced by PTEN deactivation rate
    c1=2*10^-4;              % 2*10^-4           cytoplasmic AKT deactivation rate
    c2=1*10^-4;              % 1*10^-4           cytoplasmic MDM2 dephosphorylation rate 
    c3=0;                    % 0                 nuclear P53 dephosphorylation rate
    
%pi - production rate

    p1=200;       % 200     (21.5 IET); %(100 JTB);  synthesis of apoptotic factors                  
    Th=0.65;      % 0.65    Thereshold for apoptotic death                                             
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    s0=0.06;       % 0.06  MDM2 mRNA synthesis max 0.1
    s1=0.06;       % 0.06  PTEN mRNA synthesis max 0.1
  
%ti - translation rate

    t0=0.5;        % 0.5  MDM2 translation rate max 0.5
    t1=0.1;        % 0.1  PTEN translation rate max 0.5

%di - degradation rate

    d0=0.3*10^-4;   % 0.3*10^-4  MDM2 standard degradation rate
    d1=1.5*10^-4;   % 1.5*10^-4  forced by DNAdam MDM2 degradation rate (4 fold increase of deg rate)   
    d2=5*10^-5;     % 5*10^-5    cytoplasmic PTEN degradation rate
    d3=1*10^-4;     % 1*10^-4    P53 standard degradation rate
    d4=1*10^-13;    % 1*10^-13   P53 forced by MDM2pn degradation rate (power 2)
    d5=1*10^-4;     % 1*10^-4    phospho-P53 degradation rate
    d6=1*10^-14;    % 1*10^-14   phospho-P53 forced by Mdm2 degradation rate (power 2)
    d7=3*10^-4;     % 3*10^-4    MDM2 transcript degradation rate (Blattner t1/2= 15min i.e. 0.00075)
    d8=3*10^-4;     % 3*10^-4    PTEN transcript degradation rate (Blattner t1/2= 15min i.e. 0.00075)
    
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    d9=2*10^-4;     % 2*10^-4; (3*10^-5 IET) (1*10^-4 JTB);  degradation of apoptotic factors      
    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%ii - nuclear import, export rats

    i0=5*10^-4;     % 5*10^-4   phospho-MDM2 nuclear import
    e0=0*10^-4;     % 0*10^-4   phospho-MDM2 nuclear export
     
%hi - "Michaelis Menten" coefficients  

    h0=7;     % 7   for DSB depended P53 activation

% Hill's coeficients

    n0=2; % 2   for Mdm2 depended p53 degradation
    n1=2; % 2   for p53 depended gene activation

% Control's coefficients    

    q0=1*10^-4;     % 1*10^-4;      sponteneous MDM2 and PTEN gene activation rate           
    q1=5*10^-13;    % 5*10^-13;     p53 driven activation of MDM2 and PTEN genes   
    q2=3*10^-3;     % 3*10^-3;      Mdm2 and PTEN inactivation
    q3=2.5*10^-11;  % 2.5*10^-11;   p53 driven activation of apoptotic factors   
    q4=1;           % 1;            p53 driven activation of apoptotic factors     

    drep=DNASw*a5;

    NSAT=3;      % 3                                                         

%##############################################################
%#################### NFkB ####################################
%##############################################################

    kv=5;        % kv=5, 

%###### Receptors activation #######

    kb=4*10^-6;      % 4*10^-6  receptor activation rate (about 2/min at 10ng dose)
    kf=6*10^-4;      % 6*10^-4 => t1/2 = 20min  receptor inactivation rate: Grell 1998: dissociation t1/2=33min, internalization t1/2=10-20min 

%###### Promoter binding #######

    q1n=1.5*10^-7;    % 1.5*10^-7   NF-kB ataching at A20 and IkBa site
    q2n=10^-6;        % 10^-6       IkBa inducible detaching from A20 and IkBa site

%###### Transduction pathway #######

    KN=10^4;         % 10^4     total number of IKKK kinase molecules
    KNN=2*10^5;      % 2*10^5   total number of IKK kinase molecules
    ka=10^-4;        % 10^-4    IKKK kinase activation rate (at most 1/s)
    ki=0.01;         % 0.01     IKKK kinase inactivation rate

%###### A20 and IKK #######   

    AB=1;            % A20 on (or off)
    kAB=1;           % A20 TNF block on (or off)
    
    c0n=0.1;         % 0.1      inducible A20 and IkBa mRNA synthesis
    c1n=AB*c0n;      % inducible A20 mRNA synthesis 
    c3n=0.00075;     % 0.00075  A20 and IkBa mRNA degradation rate, Blattner t1/2= 15min  
    c4n=0.5;         % 0.5      A20 and IkBa translation rate
    c5n=0.0005;      % 0.0005   A20 degradation rate
    ka20=10000;      % 10000    A20 TNFR1 block
    k2=10000;        % 10000    IKKa inactivation caused by A20
    k1=5*10^-6;      % 5*10^-6  IKKn activation caused by active IKKK, (at most 1/s)
    k3=0.003;        % 0.003    IKKa inactivation 
    k4=0.0005;       % 0.0005   IKKii transformation
 
%###### IkB alpha #######

    a1n=5*10^-7;      % 5*10^-7     IkBA*NFkB association
    a2n=10^-7;        % 10^-7       IkBa phosphoryation due to action of IKKa
    a3n=5*10^-7;      % 5*10^-7     (IkBa|NFkb) phosphorylation due to action of IKKa, (at most 0.01/s) 
    tp=0.01;          % 0.01        degradation of phospho-IkBa and phospho-IkBa complexed to NF-kB 
    c5a=0.0001;       % 0.0001      IkBa degradation rate
    c6a=0.00002;      % 0.00002     spontaneous (IkBa|NFkB) degradation of IkBa  complexed to NF-kB
 
%###### Transport #######
 
    i1=0.01;         % 0.01     NFkB nuclear import
    e2a=0.05;        % 0.05     (IkBa|NFkB) nuclear export
    i1a=0.002;       % 0.002    IkBa nuclear import
    e1a=0.005;       % 0.005    IkBa nuclear export
 
%##############################################################
%#################### Hybrid ##################################
%##############################################################

    d10=2*10^-4;    % 2*10^-4 (5*10^-4 JTB) (7.5*10^-4 IET) p53 transcript degradation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    s2=0.05;        % 0.05  p53 mRNA synthesis NFkB inducible                    
    t2=0.5;         % translation max 0.5

    q5=1.5*10^-7;   % 1.5*10^-7     NF-kB ataching at p53 site
    q6=10^-6;       % 10^-6         IkBa inducible detaching from p53 site

    s3=0.05;        % 0.05          p53 mRNA synthesis independend from NFkB             

    h1=6*10^4;      % Michaelis constant

%############################################################
%## Allele and receptor numbers, DNA damage coefficient #####
%############################################################
    
NAmdm2=2;             % Number of Mdm2  alleles
NApten=2;             % Number of PTEN alleles
NAikba=2;             % Number of IKBa alleles 
NAa20=2;              % Number of A20 alleles  
NAp53=2;              % Number of p53 alleles

DNAGy=40;             % 40 Expected number of DNA breaks per Gy
Tbreaks=DNAGy*Gy/tp0; % DNA damage coefficient /length of the radiation phase

M=1000;               % number of receptors

kk=10;                % makes time step shorter in simulation phase 10 means dt=1 sec (dt/kk)