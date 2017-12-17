%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.03.2015                     %
%   CC model                                            %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function dy=CCModel(t,y,Gy,Inp1,Inp2,Inp3,RNAiMdm2,RNAiPTEN)

[d14,h2,p3,d13,d11,h1,p2,d12,DNAGy,a5,mm1,NSAT,a6,Th,d1,h0,a1,Ka,Bmax,dd,np1,nd5,nn2,na1,nc1,nk1,Ne,Ni,p1,d9,q3,q4,nc4,a0,c3,a2,c0,a3,c1,a4,c2,i0,e0,q1,q0,q2,q5,q6,s0,s1,s2,t0,t1,t2,na2,na3,nc2,nc3,nd1,nd2,d0,d2,d3,d5,d7,d8,d10,PIPtot,AKTtot,n1,NAmdm2,NApten,NAp53]=CCParm;

APIP3=y(1);
AAKT=y(2);
MDM2=y(3);
MDM2P=y(4);
PTEN=y(5);
MDM2N=y(6);
P53=y(7);
P53P=y(8);
MDM2t=y(9);
PTENt=y(10);
P53t=y(11);
GMDM2=y(12);
GPTEN=y(13);
GP53=y(14);
IMDM2=y(15);
IMDM2P=y(16);
IMDM2N=y(17);
NUT=y(18);
P53MDM2N=y(19);
P53PMDM2N=y(20);
P53UMDM2N=y(21);
P53PUMDM2N=y(22);
P53U=y(23);
P53PU=y(24);
P53UU=y(25);
P53PUU=y(26);
NUTET=y(27);
AF=y(28);
TIME=y(29);
DSB=y(30);
siRMdm2=y(31);
siRPTEN=y(32);

dy=zeros(32,1);

dAPIP3=0;
dAAKT=0;
dMDM2=0;
dMDM2P=0;
dPTEN=0;
dMDM2N=0;
dP53=0;
dP53P=0;
dMDM2t=0;
dPTENt=0;
dP53t=0;
dGMDM2=0;
dGPTEN=0;
dGP53=0;
dIMDM2=0;
dIMDM2P=0;
dIMDM2N=0;
dNUT=0;
dP53MDM2N=0;
dP53PMDM2N=0;
dP53UMDM2N=0;
dP53PUMDM2N=0;
dP53U=0;
dP53PU=0;
dP53UU=0;
dP53PUU=0;
dNUTET=0;
dAF=0;
dTIME=0;
dDSB=0;
dsiRMdm2=0;
dsiRPTEN=0;

dAPIP3=a2*(PIPtot-APIP3)-c0*PTEN*APIP3; %APIP
dAAKT=a3*(AKTtot-AAKT)*APIP3-c1*AAKT; %AAkt
dMDM2=t0*MDM2t+c2*MDM2P+nc1*IMDM2-a4*MDM2*AAKT-(d0+d1*(DSB^2/(DSB^2+h0^2)))*MDM2-na1*MDM2*NUT; %Mdm2
dMDM2P=a4*MDM2*AAKT+e0*MDM2N+nc1*IMDM2P-c2*MDM2P-i0*MDM2P-(d0+d1*(DSB^2/(DSB^2+h0^2)))*MDM2P-na1*MDM2P*NUT; %Mdm2p
dPTEN=t1*PTENt-d2*PTEN; %PTEN
dMDM2N=i0*MDM2P+nc1*IMDM2N+nc2*P53MDM2N+nc2*P53PMDM2N+nc3*P53MDM2N+nc3*P53PMDM2N+nc2*P53UMDM2N+nc2*P53PUMDM2N+nc3*P53UMDM2N+nc3*P53PUMDM2N-e0*MDM2N-(d0+d1*(DSB^2/(DSB^2+h0^2)))*MDM2N-na1*MDM2N*NUT-na2*P53*MDM2N-na3*P53P*MDM2N-na2*P53U*MDM2N-na3*P53PU*MDM2N; %Mdm2n
dP53=t2*P53t+c3*P53P+nc3*P53MDM2N+nc4*P53U-(d3+na2*MDM2N)*P53-(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53; %p53
dP53P=(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53+nc3*P53PMDM2N+nc4*P53PU-(d5+na3*MDM2N)*P53P-c3*P53P; %p53p
dMDM2t=s0*GMDM2-d7*MDM2t-d11*MDM2t*siRMdm2/(siRMdm2+h1); %Mdm2t
dPTENt=s1*GPTEN-d8*PTENt-d14*PTENt*siRPTEN/(siRPTEN+h2); %PTENt
dP53t=s2*GP53-d10*P53t; %p53t
dGMDM2=(q0+q1*P53P^n1)*(NAmdm2-GMDM2)-q2*GMDM2; %GMdm2
dGPTEN=(q0+q1*P53P^n1)*(NApten-GPTEN)-q2*GPTEN; %GPTEN
dGP53=q5*(NAp53-GP53)-q6*GP53; %Gp53
dIMDM2=na1*MDM2*NUT+c2*IMDM2P-a4*IMDM2*AAKT-(d0+d1*(DSB^2/(DSB^2+h0^2)))*IMDM2-nc1*IMDM2; %iMdm2
dIMDM2P=na1*MDM2P*NUT+a4*IMDM2*AAKT+e0*IMDM2N-c2*IMDM2P-i0*IMDM2P-(d0+d1*(DSB^2/(DSB^2+h0^2)))*IMDM2P-nc1*IMDM2P; %iMdm2p
dIMDM2N=na1*MDM2N*NUT+i0*IMDM2P-e0*IMDM2N-(d0+d1*(DSB^2/(DSB^2+h0^2)))*IMDM2N-nc1*IMDM2N; %iMdm2n

%Vassilev Case
dNUT=Ne*(Inp2*(-(1+Ka*Bmax-Ka*Inp3)+((1+Ka*Bmax-Ka*Inp3)^2+4*Ka*Inp3)^0.5)/(2*Ka))/(nk1+(Inp2*(-(1+Ka*Bmax-Ka*Inp3)+((1+Ka*Bmax-Ka*Inp3)^2+4*Ka*Inp3)^0.5)/(2*Ka)))+nc1*IMDM2+nc1*IMDM2P+nc1*IMDM2N-Ni*NUT-na1*MDM2*NUT-na1*MDM2P*NUT-na1*MDM2N*NUT; %Nut
%Zhang Case
%dNUT=Ne*(Inp2*(-(1+Ka*Bmax-Ka*NUTET)+((1+Ka*Bmax-Ka*NUTET)^2+4*Ka*NUTET)^0.5)/(2*Ka))/(nk1+(Inp2*(-(1+Ka*Bmax-Ka*NUTET)+((1+Ka*Bmax-Ka*NUTET)^2+4*Ka*NUTET)^0.5)/(2*Ka)))+nc1*IMDM2+nc1*IMDM2P+nc1*IMDM2N-Ni*NUT-na1*MDM2*NUT-na1*MDM2P*NUT-na1*MDM2N*NUT; %Nut

dP53MDM2N=na2*P53*MDM2N-nc2*P53MDM2N-nc3*P53MDM2N; %p53Mdm2n
dP53PMDM2N=na3*P53P*MDM2N-nc2*P53PMDM2N-nc3*P53PMDM2N; %p53pMdm2n
dP53UMDM2N=na2*P53U*MDM2N-nc2*P53UMDM2N-nc3*P53UMDM2N; %p53uMdm2n
dP53PUMDM2N=na3*P53PU*MDM2N-nc2*P53PUMDM2N-nc3*P53PUMDM2N; %p53puMdm2n
dP53U=nc2*P53MDM2N+nc3*P53UMDM2N+nc4*P53UU+c3*P53PU-na2*P53U*MDM2N-nd1*P53U-nc4*P53U-(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53U; %p53u
dP53PU=(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53U+nc2*P53PMDM2N+nc3*P53PUMDM2N+nc4*P53PUU-na3*P53PU*MDM2N-nd1*P53PU-nc4*P53PU-c3*P53PU; %p53pu
dP53UU=nc2*P53UMDM2N+c3*P53PUU-nd2*P53UU-nc4*P53UU-(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53UU; %p53uu
dP53PUU=(a0+a1*(DSB^2/(DSB^2+h0^2)))*P53UU+nc2*P53PUMDM2N-nd2*P53PUU-nc4*P53PUU-c3*P53PUU; %p53puu
dNUTET=np1*Inp1*dd*exp(-dd*TIME)-nd5*((-(1+Ka*Bmax-Ka*NUTET)+((1+Ka*Bmax-Ka*NUTET)^2+4*Ka*NUTET)^0.5)/(2*Ka))/(nn2+((-(1+Ka*Bmax-Ka*NUTET)+((1+Ka*Bmax-Ka*NUTET)^2+4*Ka*NUTET)^0.5)/(2*Ka))); %Nutet
dAF=p1*q3*(P53P^n1)/(q4+q3*P53P^n1)-d9*AF; %AF
dTIME=Inp2; %Time
%dDSB=DNAGy*Gy-(a5*P53P^n1)*(DSB)/(DSB+NSAT); % IET Without apoptosis
dDSB=DNAGy*Gy-(a5*P53P^n1)*(DSB)/(DSB+NSAT)+a6*((AF*d9/p1)-Th+1);         % DNA damage level
%dDSB=DNAGy*Gy-((a5*P53P^n1)/(mm1^n1+P53P^n1))*(DSB)/(DSB+NSAT)+a6*(sign((AF*d9/p1)-Th)+1);         % DNA damage level
%dDSB=DNAGy*Gy-(a5*q3*(P53P^n1)/(q4+q3*P53P^n1))*(DSB)/(DSB+NSAT)+a6*(sign((AF*d9/p1)-Th)+1);         % DNA damage level
dsiRMdm2=p2*RNAiMdm2-d12*siRMdm2;     % Mdm2 siRNA NEW
dsiRPTEN=p3*RNAiPTEN-d13*siRPTEN;     % Mdm2 siRNA NEW

%dsiRPTEN=
%CCBcount
%CCB

%-d14*PTENt*siRPTEN/(siRPTEN+h2);


dy(1)=dAPIP3;
dy(2)=dAAKT;
dy(3)=dMDM2;
dy(4)=dMDM2P;
dy(5)=dPTEN;
dy(6)=dMDM2N;
dy(7)=dP53;
dy(8)=dP53P;
dy(9)=dMDM2t;
dy(10)=dPTENt;
dy(11)=dP53t;
dy(12)=dGMDM2;
dy(13)=dGPTEN;
dy(14)=dGP53;
dy(15)=dIMDM2;
dy(16)=dIMDM2P;
dy(17)=dIMDM2N;
dy(18)=dNUT;
dy(19)=dP53MDM2N;
dy(20)=dP53PMDM2N;
dy(21)=dP53UMDM2N;
dy(22)=dP53PUMDM2N;
dy(23)=dP53U;
dy(24)=dP53PU;
dy(25)=dP53UU;
dy(26)=dP53PUU;
dy(27)=dNUTET;
dy(28)=dAF;
dy(29)=dTIME;
dy(30)=dDSB;
dy(31)=dsiRMdm2;
dy(32)=dsiRPTEN;