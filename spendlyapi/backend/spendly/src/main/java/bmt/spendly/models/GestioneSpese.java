package bmt.spendly.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GestioneSpese {
   
    public static Map<String, Double> creaDizionario(List<Cost> listaSpese) {
        Map<String, Double> creditScore = new HashMap<>();

        for (Cost item : listaSpese) {
            String pagante = item.getUser().getUsername();       
            List<String> beneficiari = item.getGroup().getUsernames();
            System.out.println(beneficiari.toString());
            double importo = item.getImporto();
            double quota = importo / beneficiari.size();

            creditScore.putIfAbsent(pagante, 0.0);

            if (beneficiari.contains(pagante)) {
                creditScore.put(pagante, creditScore.get(pagante) + (importo - quota));
                beneficiari.remove(pagante);
            } else {
                creditScore.put(pagante, creditScore.get(pagante) + importo);
            }

            for (String beneficiario : beneficiari) {
                creditScore.putIfAbsent(beneficiario, 0.0);
                creditScore.put(beneficiario, creditScore.get(beneficiario) - quota);
            }
        }

        return creditScore;
    }

    public static List<Transazione> calcolaDebiti(List<Cost> listaSpese) {
        Map<String, Double> creditScore = creaDizionario(listaSpese);

        List<Map.Entry<String, Double>> creditori = new ArrayList<>();
        List<Map.Entry<String, Double>> debitori = new ArrayList<>();

        for (Map.Entry<String, Double> entry : creditScore.entrySet()) {
            if (entry.getValue() > 0) {
                creditori.add(entry);
            } else if (entry.getValue() < 0) {
                debitori.add(entry);
            }
        }

        debitori.sort(Comparator.comparingDouble(Map.Entry::getValue)); // Ordinamento crescente

        List<Transazione> transazioni = new ArrayList<>();

        for (Map.Entry<String, Double> creditore : creditori) {
            double credito = creditore.getValue();
            Iterator<Map.Entry<String, Double>> iter = debitori.iterator();
        
            while (iter.hasNext() && credito > 0) {
                Map.Entry<String, Double> debitore = iter.next();
                double debitoAssoluto = Math.abs(debitore.getValue());
        
                if (debitoAssoluto <= credito) {
                    transazioni.add(new Transazione(debitore.getKey(), creditore.getKey(), debitoAssoluto));
                    credito -= debitoAssoluto;
                    iter.remove(); // Usa l'iterator per rimuovere l'elemento senza problemi
                } else {
                    transazioni.add(new Transazione(debitore.getKey(), creditore.getKey(), credito));
                    debitore.setValue(debitore.getValue() + credito);
                    credito = 0;
                }
            }
        }
        System.out.println(transazioni.toString());
        return transazioni;
    }

}
