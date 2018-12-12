using Microsoft.Kinect;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Microsoft.Samples.Kinect.ControlsBasics
{
    class fit
    {
        private Movimiento19 mov1;
        int cont;

        String estado;

        public fit()
        {
            mov1 = new Movimiento19();
            estado = "Correcto";
        }
        public String getEstado()
        {
            return estado;
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
