%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.03.2015                     %
%   CC parameters                                       %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function[d14,h2,p3,d13,d11,h1,p2,d12,DNAGy,a5,mm1,NSAT,a6,Th,d1,h0,a1,Ka,Bmax,dd,np1,nd5,nn2,na1,nc1,nk1,Ne,Ni,p1,d9,q3,q4,nc4,a0,c3,a2,c0,a3,c1,a4,c2,i0,e0,q1,q0,q2,q5,q6,s0,s1,s2,t0,t1,t2,na2,na3,nc2,nc3,nd1,nd2,d0,d2,d3,d5,d7,d8,d10,PIPtot,AKTtot,n1,NAmdm2,NApten,NAp53]=CCParm;

%siRNA

d11=1.06*10^-3;  % 3.5*10^-3 8*10^-4 without MM; Mdm2 transcript caused by siRNA degradation rate
h1=8600;    % M-M for siRNA caused Mdm2t degradation
d14=1.06*10^-3;  % 3.5*10^-3 8*10^-4 without MM; PTEN transcript caused by siRNA degradation rate
h2=8600;    % M-M for siRNA caused PTENt degradation

p2=0.2;        % Mdm2 siRNA from RNAi production rate - set 0 if injection case
d12=2*10^-3;  % ok 8 min Mdm2 siRNA degradation rate
p3=0.2;        % PTEN siRNA from RNAi production rate - set 0 if injection case
d13=2*10^-3;  % ok 8 min PTEN siRNA degradation rate

%DSB
DNAGy=0.12;     % DSB/Gy occurence
a5=1.2*10^(-13);  % 1.5*10^(-13); 1*10^(-2); DSB repair rate
mm1=1*10^4;          % 2*10^5 p53 MM for DSB repair
NSAT=3;         % DSB MM for DSB repair
a6=1*10^(-2);   % AF caused DSB damage rate
Th=0.15;        % Apoptotic Th
d1=1.5*10^(-4); % DSB caused Mdm2 degradation
h0=7;           % MM for DSB caused Mdm2 deg. and p53 activ.
a1=1*10^(-3);    % DSB caused p53 activation.

%Nutlin and Core
Ka=3*10^(5);		% 3*10^5 for Vassilev 0.085*10^(6) for Zhang
Bmax=1.8*10^(-5);		% 1.8*10^-5 for Vassilev 286*10^(-6) for Zhang
dd=8.6*10^(-5);		% 8.6*10^-5 coef for external total Nutlin
np1=2*10^(-6);		% 2*10^(-6) or inject=1.8*10^(-8) external Nutlin level increase rate
nd5=2.7*10^(-1);		% 2.7*10^(-1) external Nutlin level decrease rate
nn2=50;				% 50 M-M for external Nutlin decrease
na1=6*10^(-5);		% 6*10^(-5) Mdm2 inactivation by Nutlin rate
nc1=20*10^(-2);		% 20* Mdm2 spontaneous activation
nk1=1.5;				% 1.1 M-M coefficient for Nutlin
Ne=0.19*10^9;			% 0.25*10^9 Max rate of Nutlin enter to cell
Ni=5*10^(-3);			% 5*10^(-3) Nutlin removal from cell rate
p1=200;					% synthesis of apoptotic factors
d9=2*10^(-4);			% degradation of apoptotic factors
q3=2.7*10^(-11);		% p53 driven activation of apoptotic factors
q4=1;					% p53 driven activation of apoptotic factors
nc4=0.03;			% 0.03 p53u and p53uu deubiquitilation rate
a0=1*10^(-4);			% 1*10^(-4) nuclear P53 standard phosphorylation rate
c3=8*10^(-5);			% nuclear P53 dephosphorylation rate
a2=5*10^(-5);			% cytoplasmic PIP3 activation rate     
c0=2*10^(-9);			% cytoplasmic PIP3 forced by PTEN deactivation rate
a3=2*10^(-9);			% cytoplasmic AKT activation by PIP3 rate
c1=1*10^(-2);			% cytoplasmic AKT deactivation rate
a4=22.5*10^(-8);		% cytoplasmic MDM2 phosphorylation rate    
c2=1*10^(-4);			% cytoplasmic MDM2 dephosphorylation rate
i0=5*10^(-4);			% phospho-MDM2 nuclear import
e0=0*10^(-4);			% phospho-MDM2 nuclear export
q1=10*10^(-13);			% 15*10^(-13) p53 driven activation of MDM2 and PTEN genes   
q0=2*10^(-5);			% 2*10^(-5) sponteneous MDM2 and PTEN genes activation rate   
q2=4*10^(-3);			% Mdm2 and PTEN genes inactivation
q5=3*10^(-5);			% 4*10^(-5) p53 gene activation rate CHANGE q_{0p}   
q6=8*10^(-4);			% p53 gene inactivation rate
s0=0.1;					% 0.16 *0.1 Levin 2000 MDM2 mRNA synthesis max 0.16
s1=0.1;				% *0.1 Levin 2000 PTEN mRNA synthesis max 0.16
s2=0.1;					% *0.07 Levin 2000 p53 mRNA synthesis max 0.16
t0=0.15;					% *0.3 *0.5 Levin 2000 MDM2 translation rate max 0.5
t1=0.005;				% *0.005 Levin 2000 PTEN translation rate max 0.5
t2=0.03;				% 0.05 *0.13 *0.2 Levin 2000 p53  translation max 0.5
na2=8.3*10^(-6);		% * Schon2002 p53-Mdm2n complex creation rate
na3=8.3*10^(-7);		% *# assumption based on Schon2002 p53p-Mdm2n complex creation rate
nc2=0.08;			% * Lai2001 p53-Mdm2 complex dissolution with p53 ubiquitilation
nc3=2;				% * Schon2002 p53-Mdm2 complex dissolution without p53 ubiquitilation
nd1=1.93*10^(-4);		% * Barboza2008 (60 min) base 2*10^(-4) p53u degradation rate
nd2=1.93*10^(-2);		% * Barboza2008 base 8.5*10^(-4) p53uu degradation rate
d0=3.85*10^(-4);		% * 3.85*10^-4 Stommel2004 MDM2 standard degradation rate
d2=1.93*10^(-5);		% * Yang2009 cytoplasmic PTEN degradation rate
d3=1.93*10^(-5);		% * Tang2006 p53 standard degradation rate
d5=1.93*10^(-5);		% * Tang 2006 phospho-P53 degradation rate
d7=6.15*10^(-5);		% * Sharova2009 MDM2 transcript degradation rate
d8=4.81*10^(-5);		% * Yang2009 PTEN transcript degradation rate
d10=2.13*10^(-5);	% * Mamcarz2003 p53 transcript degradation
PIPtot=8*10^(5);		% * Gray2003 Total number of PIP molecules
AKTtot=3.4*10^(4);		% * Atrih2009 Total number of Akt molecules
n1=2;					% * CK Hills for p53 depended gene activation
NAmdm2=2;				% * CK Number of Mdm2  alleles
NApten=2;				% * CK Number of  PTEN alleles
NAp53=2;				% * CK Number of p53 alleles
%e=2.71828;			% Euler constant