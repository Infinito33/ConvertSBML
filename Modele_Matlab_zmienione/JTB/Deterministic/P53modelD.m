%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Function includes system of ODEs describing         %
%   P53|MDM2 pathway                                    %
%   Present molecules are coded as follows:             %
%                                                       %
%   y(1)  PIP3a active form of PIP3 (PIP3a)             %
%   y(2)  AKTa active form of AKT (AKTa)                %
%   y(3)  MDM2 cytoplasmic (MDM2)                       %
%   y(4)  phospho-MDM2 cytoplasmic (MDM2p)              %
%   y(5)  PTEN cytoplasmic (PTEN)                       %
%   y(6)  phospho-MDM2 nuclear (MDM2pn)                 %
%   y(7)  P53 nuclear (P53n)                            %
%   y(8)  phospho-P53 nuclear (P53p)                    %
%   y(9)  MDM2 transcript (MDM2t)                       %
%   y(10) PTEN transcript (PTENt)                       %
%   y(11) Apoptotic factors                             %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

function dy=P53modelD(t,y,IR,PTENSw,DNASw,te,Tbreaks,ExtSw)

[a6,q3,d9,p1,a0 a1 a2 a3 a4 a5 c0 c1 c2 c3 p0 s0 s1 t0 t1 d0 d1 d2 d3 d4 d5 d6 d7 d8 i0 e0 h0 h1 n0 n1 AKTtot PIPtot drep q0 q0M q0P q1 q2 NSAT]=P53parametersD(te,DNASw,ExtSw);

PPA=(q0+q1*y(8)^n1)/(q2+q0+q1*y(8)^n1);
PPAA=(q3*y(8)^n1)/(q2+q3*y(8)^n1);

dy=zeros(12,1);

dy(1)=a2*(PIPtot-y(1))-c0*y(5)*y(1);                                              % active PIP3
dy(2)=a3*(AKTtot-y(2))*y(1)-c1*y(2);                                              % active cytoplasmic AKT
dy(3)=t0*y(9)-a4*y(3)*y(2)+c2*y(4)-(d0+d1*(y(11)^2/(y(11)^2+h0^2)))*y(3);         % cytoplasmic MDM2
dy(4)=a4*y(3)*y(2)-c2*y(4)-i0*y(4)-(d0+d1*(y(11)^2/(y(11)^2+h0^2)))*y(4)+e0*y(6); % cytoplasmic phospho-MDM2
dy(5)=t1*y(10)-d2*y(5);                                                           % cytoplasmic PTEN
dy(6)=i0*y(4)-(d0+d1*(y(11)^2/(y(11)^2+h0^2)))*y(6)-e0*y(6);                      % nuclear phospho-MDM2
dy(7)=p0-(a0+a1*(y(11)^2/(y(11)^2+h0^2)))*y(7)+c3*y(8)-(d3+d4*y(6)^n0)*y(7);      % nuclear P53
dy(8)=(a0+a1*(y(11)^2/(y(11)^2+h0^2)))*y(7)-c3*y(8)-(d5+d6*y(6)^n0)*y(8);         % nuclear phospho-P53 (P53p)
dy(9)=2*s0*PPA-d7*y(9);                                                           % MDM2 transcript
dy(10)=2*s1*PTENSw*PPA-d8*y(10);                                                  % PTEN transcript
dy(11)=Tbreaks*IR-(y(11)*drep*PPA)/(y(11)+NSAT*PPA)+a6*(y(12)*10^-6)^4  ;         % DNA damage level
dy(12)=p1*PPAA-d9*y(12);                                                          % Apoptotic factors