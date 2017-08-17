package com.aniruddhfichadia.presentableexample;


import com.tierable.stasis.AndroidViewPreservationConfiguration;
import com.tierable.stasis.StasisPreservationMapping;
import com.tierable.stasis.StasisPreservationStrategyView;


/**
 * @author Aniruddh Fichadia
 * @date 2017-07-28
 */
@StasisPreservationMapping(StasisPreservationStrategyView.class)
public interface PresentableStasisConfiguration
        extends AndroidViewPreservationConfiguration {
}
