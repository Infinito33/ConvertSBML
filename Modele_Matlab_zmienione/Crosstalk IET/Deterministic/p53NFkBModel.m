%#############################################
%                                            #
% p53-NFkB deterministic simulations - model #
%                                            #
% KP 02.03.2009                              #
%                                            #
%#############################################

function dy=p53NFkBModel(t,y,DNASw,ExtSw,TR,IR,Gy,tp0)

[Th,a0,a1,a1n,a2,a2n,a3,a3n,a4,a5,a6,c0,c1,c1n,c2,c3,c3n,c4n,c5a,c5n,c6a,d0,d1,d2,d3,d4,d5,d6,d7,d8,d9,d10,e0,e1a,e2a,h0,h1,i0,i1,i1a,k1,k2,k3,k4,ka20,kv,n0,n1,p1,s0,s1,s2,s3,t0,t1,t2,q0,q1,q1n,q2,q2n,q3,q4,q5,q6,drep,NSAT,tp,KN,KNN,ka,AB,kAB,ki,kb,kf,AKTtot,PIPtot,M,Tbreaks,NAmdm2,NApten,NAp53,NAa20,NAikba]=p53NFkBParam(DNASw,ExtSw,Gy,tp0);

PPAA=(q3*y(10)^n1)/(q4+q3*y(10)^n1);                                                % for apoptotic factors

dy=zeros(38,1);

dy(1)=c0*y(7)*y(2)-a2*y(1);                                                         % inactive PIP3 (PIP2)
dy(2)=a2*y(1)-c0*y(7)*y(2);                                                         % active PIP3
dy(3)=c1*y(4)-a3*y(3)*y(2);                                                         % inactive cytoplasmic AKT         
dy(4)=a3*y(3)*y(2)-c1*y(4);                                                         % active cytoplasmic AKT
dy(5)=t0*y(11)-a4*y(5)*y(4)+c2*y(6)-(d0+d1*(y(32)^2/(y(32)^2+h0^2)))*y(5);          % cytoplasmic MDM2
dy(6)=a4*y(5)*y(4)-c2*y(6)-i0*y(6)-(d0+d1*(y(32)^2/(y(32)^2+h0^2)))*y(6)+e0*y(8);   % cytoplasmic phospho-MDM2
dy(7)=t1*y(12)-d2*y(7);                                                             % cytoplasmic PTEN
dy(8)=i0*y(6)-(d0+d1*(y(32)^2/(y(32)^2+h0^2)))*y(8)-e0*y(8);                        % nuclear phospho-MDM2
dy(9)=t2*y(31)-(a0+a1*(y(32)^2/(y(32)^2+h0^2)))*y(9)+c3*y(10)-(d3+d4*y(8)^n0)*y(9); % nuclear P53
dy(10)=(a0+a1*(y(32)^2/(y(32)^2+h0^2)))*y(9)-c3*y(10)-(d5+d6*y(8)^n0)*y(10);        % nuclear phospho-P53 (P53p)
dy(11)=s0*y(34)-d7*y(11);                                                           % MDM2 transcript
dy(12)=s1*y(35)-d8*y(12);                                                           % PTEN transcript
dy(13)=p1*PPAA-d9*y(13);                                                            % Apoptotic factors

 %###############################################################
 
 dy(14)=ki*y(15)-ka*y(33)*y(14)*ka20/(ka20+kAB*y(24));                              % inactive IKKK                    
 dy(15)=ka*y(33)*y(14)* ka20/(ka20+kAB*y(24))-ki*y(15);                             % active IKKK kinase 
 dy(16)=-y(15)*k1*y(16)+k4*y(19);                                                   % neutral IKK   
 dy(17)=y(15)*k1*y(16)-k3*y(17)*(k2+y(24))/k2;                                      % free active IKK                                                                                    
 dy(18)=k3*y(17)*(k2+y(24))/k2-k4*y(18);                                            % inactive IKK (IKKi)
 dy(19)=k4*y(18)-k4*y(19);                                                          % second inactive IKK (IKKii)       
 dy(20)=a2n*y(17)*y(26)-tp*y(20);                                                   % Phospo-IkBa cytoplasmic 
 dy(21)=a3n*y(17)*y(29)-tp*y(21);                                                   % cytoplasmic (phospho-IkBa|NF-kB) 
 dy(22)=c6a*y(29)-a1n*y(22)*y(26)+tp*y(21)-i1*y(22);                                % free cytoplasmic NFkB
 dy(23)=i1*y(22)-a1n*kv*y(27)*y(23);                                                % free nuclear NFkB
 dy(24)=c4n*y(25)-c5n*y(24);                                                        % cytoplasmic A20
 dy(25)=(h1/(h1+y(10)))*c1n*y(37)-c3n*y(25);                                        % A20 transcript
 dy(26)=-a2n*y(17)*y(26)-a1n*y(26)*y(22)+c4n*y(28)-c5a*y(26)-i1a*y(26)+e1a*y(27);   % free cytoplasmic IkBa
 dy(27)=-a1n*kv*y(27)*y(23)+i1a*y(26)-e1a*y(27);                                    % free nuclear IkBan
 dy(28)=(h1/(h1+y(10)))*c1n*y(38)-c3n*y(28);                                        % IkBa transcript
 dy(29)=a1n*y(26)*y(22)-c6a*y(29)-a3n*y(17)*y(29)+e2a*y(30);                        % cytoplasmic (IkBa|NFkB) complex
 dy(30)=a1n*kv*y(27)*y(23)-e2a*y(30);                                               % nuclear (IkBa|NFkB) complex
 
%##################################################################

dy(31)=s3*NAp53+s2*y(36)-d10*y(31);                                                     % p53 transcript
dy(32)=Tbreaks*IR -drep*y(10)^2*(y(32))/(y(32)+NSAT)+ a6*((sign((y(13)*d9/p1)-Th))+1);  % DNA damage level      
dy(33)=kb*(M-y(33))*TR-kf*y(33);                                                        % Active receptors

dy(34)=(q0+q1*y(10)^n1)*(NAmdm2-y(34))-q2*y(34);        % Gmdm2                                                    
dy(35)=(q0+q1*y(10)^n1)*(NApten-y(35))-q2*y(35);        % Gpten                                                    
dy(36)=(q1n*y(23))*(NAp53-y(36))-(q2n*y(27))*y(36);     % Gp53                                                     
dy(37)=(q1n*y(23))*(NAa20-y(37))-(q2n*y(27))*y(37);     % Ga20                                                   
dy(38)=(q1n*y(23))*(NAikba-y(38))-(q2n*y(27))*y(38);    % Gikba                                                 