%##################################################
%                                                 #
% p53-NFkB stochastic simulations - status change #
%                                                 #
% KP 02.03.2009                                   #
%                                                 #
%##################################################

function [Tchange,NB,Amdm2,Apten,Ap53,Aikba,Aa20,B]=p53NFkBStatusChange(NBX,Amdm2X,AptenX,Ap53X,AikbaX,Aa20X,BX,DNASw,ExtSw,Lp53,LNFkB,LIkBa,LAF,IR,TR,dt,Gy,tp0);

[Th,a0,a1,a1n,a2,a2n,a3,a3n,a4,a5,a6,c0,c1,c1n,c2,c3,c3n,c4n,c5a,c5n,c6a,d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,e0,e1a,e2a,h0,h1,i0,i1,i1a,k1,k2,k3,k4,ka20,kv,n0,n1,p1,s0,s1,s2,s3,t0,t1,t2,q0,q1,q1n,q2,q2n,q3,q4,q5,q6,drep,NSAT,tp,KN,KNN,ka,AB,kAB,ki,kb,kf,AKTtot,PIPtot,M,Tbreaks,NAmdm2,NApten,NAp53,NAa20,NAikba,kk]=p53NFkBParam(DNASw,ExtSw,Gy,tp0);

NB=NBX;
Amdm2=Amdm2X;
Apten=AptenX;
Ap53=Ap53X;
Aikba=AikbaX;
Aa20=Aa20X;
B=BX;

%##############################################################
%############### WHEN is the change  ##########################
%##############################################################

ro=Tbreaks*IR+a6*((sign((LAF*d9/p1)-Th))+1)+(drep*Lp53.^n1).*(NB./(NB+NSAT))+kb*(M-B)*TR+kf*B+(q0+q1*Lp53.^n1).*(NAmdm2-Amdm2)+q2*Amdm2+(q0+q1*Lp53.^n1).*(NApten-Apten)+q2*Apten+(q1n*LNFkB).*(NAp53-Ap53)+(q2n*LIkBa).*Ap53+(q1n*LNFkB).*(NAa20-Aa20)+(q2n*LIkBa).*Aa20+(q1n*LNFkB).*(NAikba-Aikba)+(q2n*LIkBa).*Aikba;

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

p1=Tbreaks*IR+a6*((sign((LAF(Tchange)*d9/p1)-Th))+1);   % DNA damage
p2=(drep*Lp53(Tchange)^n1)*(NB/(NB+NSAT));              % DNA repair
p3=kb*(M-B)*TR;                                         % receptor activation
p4=kf*B;                                                % receptor inactivation
p5=(q0+q1*Lp53(Tchange)^n1)*(NAmdm2-Amdm2);             % Mdm2 gene on
p6=q2*Amdm2;                                            % Mdm2 gene off
p7=(q0+q1*Lp53(Tchange)^n1)*(NApten-Apten);             % Pten gene on
p8=q2*Apten;                                            % Pten gene off
p9=(q1n*LNFkB(Tchange))*(NAp53-Ap53);                   % p53 gene on
p10=(q2n*LIkBa(Tchange))*Ap53;                          % p53 gene off
p11=(q1n*LNFkB(Tchange))*(NAa20-Aa20);                  % A20 gene on
p12=(q2n*LIkBa(Tchange))*Aa20;                          % A20 gene off
p13=(q1n*LNFkB(Tchange))*(NAikba-Aikba);                % IkBa gene on
p14=(q2n*LIkBa(Tchange))*Aikba;                         % IkBa gene off

ss=p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12+p13+p14;

p1=p1/ss;
p2=p2/ss;
p3=p3/ss;
p4=p4/ss;
p5=p5/ss;
p6=p6/ss;
p7=p7/ss;
p8=p8/ss;
p9=p9/ss;
p10=p10/ss;
p11=p11/ss;
p12=p12/ss;
p13=p13/ss;
p14=p14/ss;

rnumber=rand;
if (rnumber<p1)                                                                                                     NB=NB+1;        end %DNA damage
if (rnumber>=p1)&(rnumber<p1+p2)                                                                                    NB=NB-1;        end
if (rnumber>=p1+p2)&(rnumber<p1+p2+p3)                                                                              B=B+1;          end %Receptors
if (rnumber>=p1+p2+p3)&(rnumber<p1+p2+p3+p4)                                                                        B=B-1;          end
if (rnumber>=p1+p2+p3+p4)&(rnumber<p1+p2+p3+p4+p5)                                                                  Amdm2=Amdm2+1;  end %Mdm2
if (rnumber>=p1+p2+p3+p4+p5)&(rnumber<p1+p2+p3+p4+p5+p6)                                                            Amdm2=Amdm2-1;  end
if (rnumber>=p1+p2+p3+p4+p5+p6)&(rnumber<p1+p2+p3+p4+p5+p6+p7)                                                      Apten=Apten+1;  end %PTEN
if (rnumber>=p1+p2+p3+p4+p5+p6+p7)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8)                                                Apten=Apten-1;  end
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9)                                          Ap53=Ap53+1;    end %p53
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8+p9)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9+p10)                                   Ap53=Ap53-1;    end
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8+p9+p10)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11)                           Aa20=Aa20+1;    end %A20
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12)                   Aa20=Aa20-1;    end
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12+p13)           Aikba=Aikba+1;  end %IkBa
if (rnumber>=p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12+p13)&(rnumber<p1+p2+p3+p4+p5+p6+p7+p8+p9+p10+p11+p12+p13+p14)   Aikba=Aikba-1;  end
