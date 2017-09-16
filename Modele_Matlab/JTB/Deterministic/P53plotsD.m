%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%                                                       %
%   KP last modification 01.05.2008                     %
%   Deterministic simulations of P53|MDM2 pathway       %
%   Plots                                               %
%                                                       %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

[a6,q3,d9,p1,a0 a1 a2 a3 a4 a5 c0 c1 c2 c3 p0 s0 s1 t0 t1 d0 d1 d2 d3 d4 d5 d6 d7 d8 i0 e0 h0 h1 n0 n1 AKTtot PIPtot drep q0 q0M q0P q1 q2 NSAT]=P53parametersD(te,DNASw,ExtSw);

figure(1)
set(gcf,'Color',[1,1,1])

subplot(4,3,1)
plot(T,Yav(:,5))
grid on;
title('[A], PTEN');

subplot(4,3,2)
plot(T,Yav(:,1))
grid on;
title('[B], PIPp');

subplot(4,3,3)
plot(T,Yav(:,2))
grid on;
title('[C], AKTp');

subplot(4,3,4)
plot(T,Yav(:,3))
grid on;
title('[D], MDM2');

subplot(4,3,5)
plot(T,Yav(:,4))
grid on;
title('[E], MDM2p');

subplot(4,3,6)
plot(T,Yav(:,6))
grid on;
title('[F], MDM2pn');

subplot(4,3,7)
plot(T,Yav(:,7))
grid on;
title('[G], P53n');

subplot(4,3,8)
plot(T,Yav(:,8))
grid on;
title('[H], P53pn');

subplot(4,3,9)
plot(T,Yav(:,10))
grid on;
title('[I], PTEN mRNA');

subplot(4,3,10)
plot(T,Yav(:,9))
grid on;
title('[J], MDM2 mRNA');

subplot(4,3,11)
plot(T,2*(q0+q1*Yav(:,8).^n1)./(q2+q0+q1*Yav(:,8).^n1))
grid on;
title('[K], MDM2 gene state');

subplot(4,3,12)
plot(T,2*PTENSw*(q0+q1*Yav(:,8).^n1)./(q2+q0+q1*Yav(:,8).^n1))
grid on;
grid on;
title('[L], PTEN gene state');

%#####################################################################

figure(2)
set(gcf,'Color',[1,1,1])

%plot(T,(Yav(:,7)+Yav(:,8)),'g')
plot(T,Yav(:,8),'g')
hold on
plot(T,Yav(:,6),'r')
plot(T,1000*Yav(:,11),'b')
hold off
grid on
legend('nuclear p53p','nuclear Mdm2','DNA damage*1000')
title('nuclear p53p vs nuclear Mdm2 and DNA damage level');
xlabel('Time in hours'); 

%#####################################################################

figure(3)
set(gcf,'Color',[1,1,1])
subplot(2,1,1)
plot(T,Yav(:,12),'g')
title('[A], Apoptotic factors');

subplot(2,1,2)
plot(T,Yav(:,11),'r')
title('[B], DNA damage level');