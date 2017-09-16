%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Models parameters                                   %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function [a6 q3 d9 p1 a0 a1 a2 a3 a4 a5 c0 c1 c2 c3 p0 s0 s1 t0 t1 d0 d1 d2 d3 d4 d5 d6 d7 d8 i0 e0 h0 h1 n0 n1 AKTtot PIPtot drep q0 q0M q0P q1 q2 NSAT]=P53parametersS(te,DNASw,ExtSw)

%total amounts
%max PTEN and MDM2 mRNA = 2 p2/d10 = 400
%max PTEN = 400 * t2 / d9 = 4*10^5 
%max MDM = 400* t1/d8 =2*10^6
%max P53 = p1/d1 =10^6 

    AKTtot=2*10^5;    % 2*10^5     total amount of AKT
    PIPtot=10^5;      % 10^5       total amount of PIP3
    PTENmax=4*10^5;   % 4*10^5     max amount of PTEN 

%ai - activation/phosphorylation rate

    a0=1*10^-4;              % 1*10^-4           nuclear P53 standard phosphorylation rate   
    a1=1*10^-3;              % 1*10^-3           nuclear P53 forced by DNAdam phosphorylation rate
    a2=5*10^-5;              % 5*10^-5           cytoplasmic PIP3 activation rate
    a3=2*10^-4/PIPtot;       % 2*10^-4/PIPtot    cytoplasmic AKT activation rate                       
    a4=2*10^-3/AKTtot;       % 2*10^-3/AKTtot    cytoplasmic MDM2 phosphorylation rate                                 
    a5=3*10^-3;              % 3*10^-3           DNA repair rate 
    
    a6=ExtSw*0.1;            %0.1                maximum apoptotic DNA degradation
    
%ci - deactivation/dephosphorylation rate

    c0=1*10^-3/PTENmax;      % 1*10^-3/PTENmax   cytoplasmic PIP3 forced by PTEN deactivation rate
    c1=2*10^-4;              % 2*10^-4           cytoplasmic AKT deactivation rate
    c2=1*10^-4;              % 1*10^-4           cytoplasmic MDM2 dephosphorylation rate 
    c3=0;                    % 0                 nuclear P53 dephosphorylation rate
    
%pi - production rate

    p0=200;        % 200   P53 production rate max 500
    p1=100;        % 100   synthesis of apoptotic factors 
    s0=0.06;       % 0.06  MDM2 mRNA synthesis max 0.1
    s1=0.06;       % 0.06  PTEN mRNA synthesis max 0.1
  
%ti - translation rate

    t0=0.5;        % 0.5  MDM2 translation rate max 0.5
    t1=0.1;        % 0.1  PTEN translation rate max 0.5

%di - degradation rate

    d0=0.3*10^-4;   % 0.5*10^-4  MDM2 standard degradation rate
    d1=1.5*10^-4;   % 1.5*10^-4  forced by DNAdam MDM2 degradation rate (4 fold increase of deg rate)   
    d2=5*10^-5;     % 5*10^-5    cytoplasmic PTEN degradation rate
    d3=1*10^-4;     % 1*10^-4    P53 standard degradation rate
    d4=1*10^-13;    % 1*10^-13   P53 forced by MDM2pn degradation rate (power 2)
    d5=1*10^-4;     % 1*10^-4    phospho-P53 degradation rate
    d6=1*10^-14;    % 1*10^-14   phospho-P53 forced by Mdm2 degradation rate (power 2)
    d7=3*10^-4;     % 3*10^-4    MDM2 transcript degradation rate (Blattner t1/2= 15min i.e. 0.00075)
    d8=3*10^-4;     % 3*10^-4    PTEN transcript degradation rate (Blattner t1/2= 15min i.e. 0.00075)

    d9=1*10^-4;     % 1*10^-4    degradation of apoptotic factors 
    
%ii - nuclear import, export rats

    i0=5*10^-4;     % 5*10^-4   phospho-MDM2 nuclear import
    e0=0*10^-4;     % 0*10^-4   phospho-MDM2 nuclear export
     
%hi - "Michaelis Menten" coefficients  

    h0=7;     % 7   for DSB depended P53 activation
    h1=7;     % 7   for DSB depended MDM2 degradation

% Hill's coeficients

    n0=2; % 2   for Mdm2 depended p53 degradation
    n1=2; % 2   for p53 depended gene activation

% Control's coefficients    

q0=1*10^-4;   % 1/(30*te);         
q0P=1*10^-4;  % 1/(30*te)         sponteneous PTEN gene activation rate   stare(tssP)
q0M=1*10^-4;  % 1/(30*te)         sponteneous Mdm2 gene activation rate   stare(tssM)           
q1=5*10^-13;  % (1/80000)^n1/te;  p53 driven activation of MDM2 and PTEN genes   
q2=3*10^-3;   % 1/te; 

q3=8*10^-14;  %(1/200000)^n1/te;  p53 driven activation of apoptotic factors

drep=DNASw*a5;

NSAT=50;      % 50
