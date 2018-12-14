using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.ControlsBasics
{
    class Detector
    {
        private Movement1 mov1;
        private Movement2 mov2;
        private Movement3 mov3;

        public Detector()
        {
            mov1 = new Movement1();
            mov2 = new Movement2();
            mov3 = new Movement3();
        }

        public int getFaseMv1()
        {
            return mov1.getFase();
        }
        public void setFase(int a)
        {
            mov1.setFase(a);
        }

        public bool deteccion(Skeleton esqueleto){

            if (mov1.movimiento(esqueleto))
            {
                if(mov1.getFase() == 2)
                {
                    mov1.setFase(0);
                    mov2.setFase(0);
                    mov3.setFase(0);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }


        }

        public bool deteccion2(Skeleton esqueleto)
        {

            if (mov2.movimiento(esqueleto))
            {
                if (mov2.getFase() == 2)
                {
                    mov2.setFase(0);
                    mov3.setFase(0);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }


        }
        public bool deteccion3(Skeleton esqueleto)
        {

            if (mov3.movimiento(esqueleto))
            {
                if (mov3.getFase() == 2)
                {
                    mov2.setFase(0);
                    mov3.setFase(0);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }


        }
    }
}
